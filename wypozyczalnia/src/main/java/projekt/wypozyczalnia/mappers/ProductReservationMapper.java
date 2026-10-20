package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.ProductReservationDto;
import projekt.wypozyczalnia.data.entities.ProductReservation;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class, OrderMapper.class})
public interface ProductReservationMapper {

    ProductReservation toEntity(ProductReservationDto productreservationDto);

    ProductReservationDto toDto(ProductReservation productreservation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductReservation partialUpdate(ProductReservationDto productreservationDto, @MappingTarget ProductReservation productreservation);
}