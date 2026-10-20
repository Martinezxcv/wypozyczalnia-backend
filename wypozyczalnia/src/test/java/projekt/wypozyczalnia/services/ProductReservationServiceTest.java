package projekt.wypozyczalnia.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projekt.wypozyczalnia.data.dtos.ProductReservationDto;
import projekt.wypozyczalnia.data.entities.ProductReservation;
import projekt.wypozyczalnia.mappers.ProductReservationMapper;
import projekt.wypozyczalnia.repositories.ProductReservationRepository;
import projekt.wypozyczalnia.servicesImpl.ProductReservationServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductReservationServiceTest {

    @InjectMocks
    private ProductReservationServiceImpl productReservationService;

    @Mock
    private ProductReservationRepository productReservationRepository;

    @Mock
    private ProductReservationMapper productReservationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isReserved_ShouldReturnTrue_WhenReservationExists() {
        // Arrange
        int productId = 1;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        when(productReservationRepository
                .countByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate)).thenReturn(1);

        // Act
        boolean result = productReservationService.isReserved(productId, startDate, endDate);

        // Assert
        assertTrue(result);
        verify(productReservationRepository, times(1))
                .countByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate);
    }

    @Test
    void isReserved_ShouldReturnFalse_WhenNoReservationExists() {
        // Arrange
        int productId = 1;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        when(productReservationRepository
                .countByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate)).thenReturn(0);

        // Act
        boolean result = productReservationService.isReserved(productId, startDate, endDate);

        // Assert
        assertFalse(result);
        verify(productReservationRepository, times(1))
                .countByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate);
    }

    @Test
    void getReservationsBetween_ShouldReturnReservations_WhenExist() {
        // Arrange
        int productId = 1;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        ProductReservation productReservation = new ProductReservation();
        ProductReservationDto dto = new ProductReservationDto();

        when(productReservationRepository
                .findByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate)).thenReturn(List.of(productReservation));

        when(productReservationMapper.toDto(productReservation)).thenReturn(dto);

        // Act
        List<ProductReservationDto> result = productReservationService.getReservationsBetween(productId, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(dto, result.get(0));  
        verify(productReservationRepository, times(1))
                .findByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate);
    }


    @Test
    void getReservationsBetween_ShouldReturnEmptyList_WhenNoReservationsExist() {
        // Arrange
        int productId = 1;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        when(productReservationRepository
                .findByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate)).thenReturn(List.of());

        // Act
        List<ProductReservationDto> result = productReservationService.getReservationsBetween(productId, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productReservationRepository, times(1))
                .findByProduct_ProductIdAndReservationStartDateLessThanEqualAndReservationEndDateGreaterThanEqual(
                        productId, endDate, startDate);
    }

    @Test
    void addReservation_ShouldSaveReservation() {
        // Arrange
        ProductReservationDto productReservationDto = new ProductReservationDto();
        when(productReservationMapper.toEntity(productReservationDto)).thenReturn(new projekt.wypozyczalnia.data.entities.ProductReservation());

        // Act
        productReservationService.addReservation(productReservationDto);

        // Assert
        verify(productReservationRepository, times(1)).save(any());
    }

    @Test
    void deleteReservations_ShouldDeleteReservations() {
        // Arrange
        int productId = 1;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        // Act
        productReservationService.deleteReservations(productId, startDate, endDate);

        // Assert
        verify(productReservationRepository, times(1))
                .deleteByProduct_ProductIdAndReservationStartDateAndReservationEndDate(productId, startDate, endDate);
    }
}