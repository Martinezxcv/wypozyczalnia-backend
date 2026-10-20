package projekt.wypozyczalnia.servicesImpl;

import com.itextpdf.io.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.dtos.OrderDTO;
import projekt.wypozyczalnia.services.OrderService;
import projekt.wypozyczalnia.services.ProductService;
import projekt.wypozyczalnia.services.ReportService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderService orderService;

    private final ProductService productService;

    @Scheduled(cron = "0 0 0 L * ?") // Runs at midnight on the last day of every month
    public void generateMonthlyReportAtEndOfMonth() {

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        byte[] pdfBytes = generateMonthlyReportAsPDF(startOfMonth, endOfMonth);

        saveMonthlyReportToFile(pdfBytes, endOfMonth);
    }

    public byte[] generateMonthlyReportAsPDF(LocalDate startOfMonth, LocalDate endOfMonth) {
        List<OrderDTO> monthlyOrders = orderService.getMonthlyOrders(startOfMonth, endOfMonth);
        Map<String, Long> productRentalCounts = productService.getProductRentalCounts(startOfMonth, endOfMonth);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            Paragraph companyInfo = new Paragraph("YOUR COMPANY\n1234 Your Street\nCity, California 90210\nUnited States\n1-888-123-4567")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10);
            document.add(companyInfo);

            Paragraph reportTitle = new Paragraph("Monthly Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold();
            document.add(reportTitle);

            Paragraph reportOverview = new Paragraph("Report for " + startOfMonth.getMonth() + " " + startOfMonth.getYear())
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(12);
            document.add(reportOverview);

            Paragraph OrdersTitle = new Paragraph("Orders:")
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(16)
                    .setBold();
            document.add(OrdersTitle);

            Table ordersTable = new Table(new float[]{3, 3, 4});
            ordersTable.setWidth(UnitValue.createPercentValue(100));
            ordersTable.addHeaderCell(new Cell().add(new Paragraph("Order ID").setBold()));
            ordersTable.addHeaderCell(new Cell().add(new Paragraph("Order Date").setBold()));
            ordersTable.addHeaderCell(new Cell().add(new Paragraph("Client Email").setBold()));

            for (OrderDTO order : monthlyOrders) {
                ordersTable.addCell(new Cell().add(new Paragraph(String.valueOf(order.getOrderId()))));
                ordersTable.addCell(new Cell().add(new Paragraph(order.getOrderDate().toString())));
                ordersTable.addCell(new Cell().add(new Paragraph(order.getCart().getUser().getEmail())));
            }

            document.add(ordersTable);
            Paragraph reportOrders = new Paragraph("Total number of orders for " + startOfMonth.getMonth() + " " + startOfMonth.getYear() + ": " + monthlyOrders.size())
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(14)
                    .setBold();
            document.add(reportOrders);

            Paragraph ProductsTitle = new Paragraph("Products:")
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(16)
                    .setBold();
            document.add(ProductsTitle);

            Table productsTable = new Table(new float[]{6, 4});
            productsTable.setWidth(UnitValue.createPercentValue(100));
            productsTable.addHeaderCell(new Cell().add(new Paragraph("Product Name").setBold()));
            productsTable.addHeaderCell(new Cell().add(new Paragraph("Number of Rentals").setBold()));

            for (Map.Entry<String, Long> entry : productRentalCounts.entrySet()) {
                productsTable.addCell(new Cell().add(new Paragraph(entry.getKey())));
                productsTable.addCell(new Cell().add(new Paragraph(String.valueOf(entry.getValue()))));
            }

            document.add(productsTable);

            Paragraph reportDetails = new Paragraph("Report generated on " + LocalDate.now())
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(12);
            Paragraph reportOverviewEnd = new Paragraph("End of Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12);
            document.add(reportDetails);
            document.add(reportOverviewEnd);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    private void saveMonthlyReportToFile(byte[] pdfBytes, LocalDate endOfMonth) {
        try {
            String fileName = "monthly_report_" + endOfMonth.getMonthValue() + "_" + endOfMonth.getYear() + ".pdf";
            Path path = Paths.get("reports", fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, pdfBytes);
            System.out.println("Monthly report saved as: " + path);
        } catch (IOException | java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
