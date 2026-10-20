package projekt.wypozyczalnia.requests;

import lombok.Getter;
import lombok.Setter;
import projekt.wypozyczalnia.data.dtos.ProductDTO;

@Getter
@Setter
public class AddProductRequest {

    private ProductDTO productDTO;
    private String categoryName;
    private String skillLevelName;
    private String equipmentTypeName;
}

