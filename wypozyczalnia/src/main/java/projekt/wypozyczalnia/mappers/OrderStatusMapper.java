package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.OrderStatusDTO;
import projekt.wypozyczalnia.data.entities.OrderStatus;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderStatusMapper {

    @Mapping(target = "orderStatusId", source = "orderStatusDTO.orderStatusId")
    @Mapping(target = "status", source = "orderStatusDTO.status")
    OrderStatus toEntity(OrderStatusDTO orderStatusDTO);

    @Mapping(target = "orderStatusId", source = "orderStatus.orderStatusId")
    @Mapping(target = "status", source = "orderStatus.status")
    OrderStatusDTO toDto(OrderStatus orderStatus);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderStatus partialUpdate(OrderStatusDTO orderStatusDTO, @MappingTarget OrderStatus orderStatus);
}