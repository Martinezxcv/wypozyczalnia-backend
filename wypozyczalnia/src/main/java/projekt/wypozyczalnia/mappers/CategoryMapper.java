package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.CategoryDTO;
import projekt.wypozyczalnia.data.entities.Category;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mappings({
            @Mapping(target = "categoryId", source = "categoryId"),
            @Mapping(target = "name", source = "name")
    })
    Category toEntity(CategoryDTO categoryDTO);

    @Mappings({
            @Mapping(target = "categoryId", source = "categoryId"),
            @Mapping(target = "name", source = "name")
    })
    CategoryDTO toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CategoryDTO categoryDTO, @MappingTarget Category category);
}