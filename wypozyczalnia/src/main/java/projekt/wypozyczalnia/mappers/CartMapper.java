package projekt.wypozyczalnia.mappers;


import org.mapstruct.Mapping;
import projekt.wypozyczalnia.data.dtos.CartDTO;
import projekt.wypozyczalnia.data.dtos.OrderStatusDTO;
import projekt.wypozyczalnia.data.entities.Cart;
import org.mapstruct.Mapper;
import projekt.wypozyczalnia.data.entities.OrderStatus;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "cartId", source = "cartId")
    CartDTO toDto(Cart cart);

    @Mapping(target = "cartId", source = "cartId")
    Cart fromDto(CartDTO cartDTO);

}
