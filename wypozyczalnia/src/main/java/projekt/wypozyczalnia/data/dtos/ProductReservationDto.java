package projekt.wypozyczalnia.data.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationDto {

    private ProductDTO product;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
}