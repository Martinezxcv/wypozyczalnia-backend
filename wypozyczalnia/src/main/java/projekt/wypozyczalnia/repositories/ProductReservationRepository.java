package projekt.wypozyczalnia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projekt.wypozyczalnia.data.entities.ProductReservation;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductReservationRepository extends JpaRepository<ProductReservation, Integer> {
    
    int countByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(int productId, LocalDate endDate, LocalDate startDate);
    List<ProductReservation> findByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(int productId, LocalDate endDate, LocalDate startDate);
    List<ProductReservation> findByProduct_ProductIdAndReservationStartDateGreaterThanEqual(int productId, LocalDate currentDate);
    void deleteByProduct_ProductIdAndReservationStartDateAndReservationEndDate(int productId, LocalDate rentalDateStart, LocalDate rentalDateEnd);
}

