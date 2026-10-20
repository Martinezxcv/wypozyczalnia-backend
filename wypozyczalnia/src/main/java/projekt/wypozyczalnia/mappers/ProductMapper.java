package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.data.entities.Product;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, EquipmentTypeMapper.class, SkillLevelMapper.class})
public interface ProductMapper {

    @Mappings({
            @Mapping(target = "category", source = "category"),
            @Mapping(target = "equipmentType", source = "equipmentType"),
            @Mapping(target = "skillLevel", source = "skillLevel"),
    })
    Product toEntity(ProductDTO productDTO);

    @Mappings({
            @Mapping(target = "category", source = "category"),
            @Mapping(target = "equipmentType", source = "equipmentType"),
            @Mapping(target = "skillLevel", source = "skillLevel"),
    })
    ProductDTO toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductDTO productDTO, @MappingTarget Product product);
}
