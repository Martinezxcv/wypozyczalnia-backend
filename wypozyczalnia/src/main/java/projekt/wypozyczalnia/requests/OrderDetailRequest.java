package projekt.wypozyczalnia.requests;

import lombok.Getter;
import lombok.Setter;
import projekt.wypozyczalnia.data.dtos.AddressDTO;
import projekt.wypozyczalnia.data.dtos.OrderDTO;
import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.data.dtos.UserDTO;

import java.util.List;

@Getter
@Setter
public class OrderDetailRequest {

    private OrderDTO orderDTO;
    private List<ProductDTO> productsDTO;
}
