package projekt.wypozyczalnia.requests;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationRequest {

    private int orderId;
    private int productId;
    private LocalDate startDate;
    private LocalDate endDate;
}
