package projekt.wypozyczalnia.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private int addressId;
    private String city;
    private String street;
    private String number;
    private String postalCode;
    private boolean isDefault;

}
