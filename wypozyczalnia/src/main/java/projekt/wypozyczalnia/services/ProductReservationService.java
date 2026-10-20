package projekt.wypozyczalnia.services;


import projekt.wypozyczalnia.data.dtos.ProductReservationDto;

import java.time.LocalDate;
import java.util.List;

public interface ProductReservationService {

    boolean isReserved(int productId, LocalDate startDate, LocalDate endDate);
    List<ProductReservationDto> getReservationsBetween(int productId, LocalDate startDate, LocalDate endDate);
    void addReservation(ProductReservationDto productReservation);
    void deleteReservations(int productId, LocalDate rentalDateStart, LocalDate rentalDateEnd);
}
