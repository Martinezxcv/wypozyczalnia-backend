package projekt.wypozyczalnia.data.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private int productId;
    private CategoryDTO category;
    private SkillLevelDTO skillLevel;
    private EquipmentTypeDTO equipmentType;
    private String model;
    private String brand;
    private String name;
    private boolean available;
    private String size;
    private float weight;
    private float price;
    private String description;
    private String imageFilepath;


}
