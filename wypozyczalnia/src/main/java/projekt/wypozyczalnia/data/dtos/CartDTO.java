package projekt.wypozyczalnia.data.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private int cartId;
    private UserDTO user;

}
