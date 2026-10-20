package projekt.wypozyczalnia.servicesImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.dtos.ProductReservationDto;
import projekt.wypozyczalnia.mappers.ProductReservationMapper;
import projekt.wypozyczalnia.repositories.ProductReservationRepository;
import projekt.wypozyczalnia.services.ProductReservationService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReservationServiceImpl implements ProductReservationService {

    private final ProductReservationRepository productReservationRepository;
    private final ProductReservationMapper productReservationMapper;

    @Override
    public boolean isReserved(int productId, LocalDate startDate, LocalDate endDate) {
        int count = productReservationRepository
                .countByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate);
        return count > 0;
    }

    @Override
    public List<ProductReservationDto> getReservationsBetween(int productId, LocalDate startDate, LocalDate endDate) {
        return productReservationRepository
                .findByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate).stream().map(productReservationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void addReservation(ProductReservationDto productReservation) {
        productReservationRepository.save(productReservationMapper.toEntity(productReservation));
    }

    @Override
    @Transactional
    public void deleteReservations(int productId, LocalDate rentalDateStart, LocalDate rentalDateEnd) {
        productReservationRepository.deleteByProduct_ProductIdAndReservationStartDateAndReservationEndDate(productId, rentalDateStart, rentalDateEnd);
    }

}
