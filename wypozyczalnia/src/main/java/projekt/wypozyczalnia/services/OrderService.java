package projekt.wypozyczalnia.services;

import projekt.wypozyczalnia.data.dtos.OrderDTO;
import projekt.wypozyczalnia.requests.OrderDetailRequest;
import projekt.wypozyczalnia.requests.OrderStatusChangeRequest;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    List<OrderDTO> getClientOrders();
    List<OrderDTO> getAllClientOrders();
    void makeOrder(LocalDate rentalDateStart, LocalDate rentalDateEnd);
    void changeOrderStatus(OrderStatusChangeRequest request);
    Float calculateProductsCost();
    OrderDetailRequest getOrderDetails(int orderId);
    void generateInvoice(int orderId);
    List<OrderDTO> getMonthlyOrders(LocalDate startDate, LocalDate endDate);
}
