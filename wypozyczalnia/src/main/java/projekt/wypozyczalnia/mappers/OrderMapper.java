package projekt.wypozyczalnia.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import projekt.wypozyczalnia.data.dtos.OrderDTO;
import projekt.wypozyczalnia.data.entities.Order;

@Mapper(componentModel = "spring", uses = {OrderStatusMapper.class, CartMapper.class, AddressMapper.class})
public interface OrderMapper {


    @Mappings({
            @Mapping(target = "orderId", source = "order.orderId"),
            @Mapping(target = "orderStatus", source = "order.orderStatus"),
            @Mapping(target = "address", source = "order.address"),
            @Mapping(target = "cart", source = "order.cart"),
            @Mapping(target = "orderDate", source = "order.orderDate"),
            @Mapping(target = "rentalDateStart", source = "order.rentalDateStart"),
            @Mapping(target = "rentalDateEnd", source = "order.rentalDateEnd"),
            @Mapping(target = "totalCost", source = "order.totalCost"),
    })
    OrderDTO toDto(Order order);

    @Mappings({
            @Mapping(target = "orderId", source = "orderDTO.orderId"),
            @Mapping(target = "orderStatus", source = "orderDTO.orderStatus"),
            @Mapping(target = "address", source = "orderDTO.address"),
            @Mapping(target = "cart", source = "orderDTO.cart"),
            @Mapping(target = "orderDate", source = "orderDTO.orderDate"),
            @Mapping(target = "rentalDateStart", source = "orderDTO.rentalDateStart"),
            @Mapping(target = "rentalDateEnd", source = "orderDTO.rentalDateEnd"),
            @Mapping(target = "totalCost", source = "orderDTO.totalCost")
    })
    Order fromDto(OrderDTO orderDTO);


}
