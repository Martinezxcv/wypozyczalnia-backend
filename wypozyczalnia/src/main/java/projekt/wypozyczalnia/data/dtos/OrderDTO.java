package projekt.wypozyczalnia.data.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private int orderId;
    private OrderStatusDTO orderStatus;
    private AddressDTO address;
    private CartDTO cart;
    private LocalDate orderDate;
    private LocalDate rentalDateStart;
    private LocalDate rentalDateEnd;
    private float totalCost;
}
