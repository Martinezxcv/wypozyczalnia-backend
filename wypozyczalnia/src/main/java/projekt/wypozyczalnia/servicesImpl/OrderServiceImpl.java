package projekt.wypozyczalnia.servicesImpl;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.dtos.OrderDTO;
import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.data.dtos.ProductReservationDto;
import projekt.wypozyczalnia.data.entities.*;
import projekt.wypozyczalnia.exceptions.ConflictReservationException;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.mappers.OrderMapper;
import projekt.wypozyczalnia.mappers.ProductMapper;
import projekt.wypozyczalnia.repositories.*;
import projekt.wypozyczalnia.requests.OrderDetailRequest;
import projekt.wypozyczalnia.requests.OrderStatusChangeRequest;
import projekt.wypozyczalnia.services.CartService;
import projekt.wypozyczalnia.services.MailService;
import projekt.wypozyczalnia.services.OrderService;
import projekt.wypozyczalnia.services.ProductReservationService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final JwtUtils jwtUtils;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartRepository cartRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ProductCartRepository productCartRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final OrderProductRepository orderProductRepository;
    private final AddressRepository addressRepository;
    private final MailService mailService;
    private final ProductMapper productMapper;
    private final ProductReservationService productReservationService;

    @Override
    public List<OrderDTO> getClientOrders() {
        int currentClientId = jwtUtils.getCurrentUserId();
        return orderRepository.findByCartUserUserIdOrderByOrderDateDesc(currentClientId).stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllClientOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void makeOrder(LocalDate rentalDateStart, LocalDate rentalDateEnd) {

        int currentUserId = jwtUtils.getCurrentUserId();
        Cart clientCart = (cartRepository.findByUserUserId(currentUserId));
        List<ProductCart> productsInCart = productCartRepository.findById_CartId_CartId(clientCart.getCartId());

        List<String> conflictingReservations = new ArrayList<>();
        productsInCart.forEach(productCart -> {
            if (productReservationService.isReserved(productCart.getId().getProductId().getProductId(), rentalDateStart, rentalDateEnd)) {

                List<ProductReservationDto> conflictingReservationDtos = productReservationService.getReservationsBetween(
                        productCart.getId().getProductId().getProductId(), rentalDateStart, rentalDateEnd
                );

                Map<String, List<String>> productReservationsGrouped = conflictingReservationDtos.stream()
                        .collect(Collectors.groupingBy(
                                reservation -> reservation.getProduct().getName(),
                                Collectors.mapping(
                                        reservation -> System.lineSeparator()+reservation.getReservationStartDate() +
                                                " <-> " + reservation.getReservationEndDate(),
                                        Collectors.toList()
                                )
                        ));

                productReservationsGrouped.forEach((productName, reservationPeriods) -> {
                    String reservationDetails = System.lineSeparator() + productName + " reserved during periods:" + System.lineSeparator() +
                            String.join("\n", reservationPeriods);
                    conflictingReservations.add(reservationDetails);
                });
            }

            else {
                ProductReservationDto newReservation = new ProductReservationDto();
                newReservation.setProduct(productMapper.toDto(productCart.getId().getProductId()));
                newReservation.setReservationStartDate(rentalDateStart);
                newReservation.setReservationEndDate(rentalDateEnd);
                productReservationService.addReservation(newReservation);

            }
        });

        if (!conflictingReservations.isEmpty()) {
            String formattedConflicts = String.join("\n\n", conflictingReservations);
            throw new ConflictReservationException("Product(s) already reserved for the selected period:\n" + formattedConflicts);
        }


        Order newOrder = new Order();
        newOrder.setOrderStatus(orderStatusRepository.findByOrderStatusId(1));
        newOrder.setCart(clientCart);
        newOrder.setOrderDate(LocalDate.now());
        newOrder.setRentalDateStart(rentalDateStart);
        newOrder.setRentalDateEnd(rentalDateEnd);
        newOrder.setAddress(addressRepository.findByUserUserIdAndIsDefaultTrue(currentUserId).orElseThrow(()->new EntityNotFoundException("Address not found")));
        int numberOfDays = calculateNumberOfDays(newOrder.getRentalDateStart(), newOrder.getRentalDateEnd());
        float totalPrice = calculateTotalPrice();
        float totalCost = (0.95f*totalPrice)+(numberOfDays*(10*getSeasonMultiplier(rentalDateStart)));
        newOrder.setTotalCost(totalCost);

        orderRepository.save((newOrder));
        updateOrderProductTable(newOrder);
        cartService.clearCart();

        cartRepository.save((clientCart));
        mailService.sendMail("Your order has been placed", "Your order has been placed successfully. You can check the details in your account.");
    }

    @Override
    public void changeOrderStatus(OrderStatusChangeRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(()->new EntityNotFoundException("Order not found"));
        OrderStatus orderStatus = orderStatusRepository.findByOrderStatusId(request.getStatusId());
        String mail = order.getCart().getUser().getEmail();
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        mailService.sendMail(mail,"Order status changed", "Your order status has been changed to: "+orderStatus.getStatus()+". You can check the details in your account.");
        if(request.getStatusId()==7 || request.getStatusId()==4 || request.getStatusId()==5){
            List<OrderProduct> orderProduct = orderProductRepository.findById_OrderId(request.getOrderId());
            for(OrderProduct product: orderProduct){
                int productId = product.getId().getProductId();
                productReservationService.deleteReservations(productId, order.getRentalDateStart(), order.getRentalDateEnd());
            }
        }
    }

    @Override
    public Float calculateProductsCost() {
        return calculateTotalPrice();
    }

    @Override
    public OrderDetailRequest getOrderDetails(int orderId) {
        OrderDTO order = orderMapper.toDto(orderRepository.findById(orderId).orElseThrow(()->new EntityNotFoundException("Order not found")));
        List<OrderProduct> orderProducts = orderProductRepository.findById_OrderId(orderId);
        List<ProductDTO> productDTOs = orderProducts.stream()
                .map(productCart -> productRepository.findById(productCart.getId().getProductId())
                        .map(productMapper::toDto)
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        OrderDetailRequest orderDetailRequest = new OrderDetailRequest();
        orderDetailRequest.setOrderDTO(order);
        orderDetailRequest.setProductsDTO(productDTOs);
        return orderDetailRequest;
    }

    @Override
    public void generateInvoice(int orderId) {
        OrderDetailRequest orderDetails = getOrderDetails(orderId);
        OrderDTO order = orderDetails.getOrderDTO();
        List<ProductDTO> products = orderDetails.getProductsDTO();

        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + "/invoice_" + orderId + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph companyInfo = new Paragraph("YOUR COMPANY\n1234 Your Street\nCity, California 90210\nUnited States\n1-888-123-4567")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10);
            document.add(companyInfo);

            Paragraph invoiceTitle = new Paragraph("Proforma Invoice")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold();
            document.add(invoiceTitle);

            Paragraph billedTo = new Paragraph("Billed To:\n" +
                    order.getCart().getUser().getFirstname() + " " + order.getCart().getUser().getLastname() + "\n" +
                    order.getAddress().getStreet() + "\n" +
                    order.getAddress().getCity() + ", " + order.getAddress().getPostalCode() + "\n" +
                    order.getCart().getUser().getPhone())
                    .setFontSize(10);
            document.add(billedTo);

            Paragraph invoiceDetails = new Paragraph("Date Issued: " + order.getOrderDate() + "\n" +
                    "Invoice Number: INV-" + orderId + "\n" +
                    "Due Date: " + order.getRentalDateEnd())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10);
            document.add(invoiceDetails);

            document.add(new Paragraph("\n"));

            float subtotal = 0;
            float rentalCost = 0;


            Table productTable = new Table(new float[]{3, 2});
            productTable.setWidth(UnitValue.createPercentValue(100));
            productTable.addHeaderCell(new Cell().add(new Paragraph("Product").setBold()));
            productTable.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()));

            int daysRented = calculateNumberOfDays(order.getRentalDateStart(), order.getRentalDateEnd());
            float seasonMultiplier = getSeasonMultiplier(order.getRentalDateStart());

            for (ProductDTO product : products) {
                float productTotal = product.getPrice();
                subtotal += productTotal;
                rentalCost = (daysRented * 10 * seasonMultiplier);

                productTable.addCell(new Cell().add(new Paragraph(product.getName())));
                productTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", product.getPrice()))));
            }
            document.add(productTable);

            document.add(new Paragraph(""));
            Paragraph rentalCostParagraph = new Paragraph("Rental Cost Calculation")
                    .setBold()
                    .setFontSize(12);
            document.add(rentalCostParagraph);

            Table rentalCostTable = new Table(new float[]{6, 2, 2, 2});
            rentalCostTable.addHeaderCell(new Cell().add(new Paragraph("Rental Cost per Day").setBold()));
            rentalCostTable.addHeaderCell(new Cell().add(new Paragraph("Season Multiplier").setBold()));
            rentalCostTable.addHeaderCell(new Cell().add(new Paragraph("Number of Days").setBold()));
            rentalCostTable.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));
            rentalCostTable.addCell(new Cell().add(new Paragraph("10").setBold()));
            rentalCostTable.addCell(new Cell().add(new Paragraph(String.format("%.1fx", seasonMultiplier))));
            rentalCostTable.addCell(new Cell().add(new Paragraph(String.valueOf(daysRented))));
            rentalCostTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", rentalCost))));

            document.add(rentalCostTable);

            float discount = subtotal * 0.05f;
            float total = subtotal + rentalCost - discount;

            document.add(new Paragraph("Summary").setBold());
            Table summaryTable = new Table(new float[]{6, 2});
            summaryTable.setWidth(UnitValue.createPercentValue(50));
            summaryTable.setHorizontalAlignment(HorizontalAlignment.LEFT);

            summaryTable.addCell(new Cell().add(new Paragraph("Subtotal").setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", subtotal+rentalCost))));

            summaryTable.addCell(new Cell().add(new Paragraph("Discount").setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("-$%.2f", discount))));

            summaryTable.addCell(new Cell().add(new Paragraph("Total").setBold()));
            summaryTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", total))));

            document.add(summaryTable);

            document.close();

            mailService.sendMailWithAttachment(
                    "Invoice for Order " + order.getOrderId(),
                    "Please find attached the invoice for your order.",
                    filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<OrderDTO> getMonthlyOrders(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream().map(orderMapper::toDto).collect(Collectors.toList());
    }


    private int calculateNumberOfDays(LocalDate start, LocalDate end) {
        return (int) ChronoUnit.DAYS.between( start, end)+1;
    }

    private float calculateTotalPrice() {

        Cart clientCart = cartRepository.findByUserUserId(jwtUtils.getCurrentUserId());
        List<ProductCart> productsInCart = productCartRepository.findById_CartId_CartId(clientCart.getCartId());
        float totalPrice = 0;

        for (ProductCart product : productsInCart){
            totalPrice += (product.getId().getProductId().getPrice());
        }

        return totalPrice;
    }

    public float getSeasonMultiplier(LocalDate date) {
        int month = date.getMonthValue();

        if (month == 12 || month == 1 || month == 2) {
            return 2.0f;
        } else if (month == 3 || month == 4 || month == 11) {
            return 1.5f;
        } else {
            return 1.0f;
        }
    }

    private void updateOrderProductTable(Order order) {

        Cart clientCart = cartRepository.findByUserUserId(jwtUtils.getCurrentUserId());
        List<ProductCart> productsInCart = productCartRepository.findById_CartId_CartId(clientCart.getCartId());

        for (ProductCart productCart : productsInCart) {

            OrderProduct orderProduct = new OrderProduct();
            OrderProductKey orderProductKey = new OrderProductKey();

            orderProductKey.setOrderId(order.getOrderId());
            orderProductKey.setProductId(productCart.getId().getProductId().getProductId());
            orderProduct.setId(orderProductKey);
            orderProduct.setOrder(order);
            orderProduct.setProduct(productRepository.findById(productCart.getId().getProductId().getProductId()).orElse(null));

            orderProductRepository.save(orderProduct);
        }
    }

}
