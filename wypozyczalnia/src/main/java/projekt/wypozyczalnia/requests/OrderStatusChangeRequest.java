package projekt.wypozyczalnia.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusChangeRequest {

    private String email;
    private int orderId;
    private int statusId;
}
