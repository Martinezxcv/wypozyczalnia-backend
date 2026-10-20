package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.EquipmentTypeDTO;
import projekt.wypozyczalnia.data.entities.EquipmentType;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EquipmentTypeMapper {

    @Mappings({
            @Mapping(target = "equipmentTypeId", source = "equipmentTypeId"),
            @Mapping(target = "name", source = "name")
    })
    EquipmentType toEntity(EquipmentTypeDTO equipmentTypeDTO);

    @Mappings({
            @Mapping(target = "equipmentTypeId", source = "equipmentTypeId"),
            @Mapping(target = "name", source = "name")
    })
    EquipmentTypeDTO toDto(EquipmentType equipmentType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EquipmentType partialUpdate(EquipmentTypeDTO equipmentTypeDTO, @MappingTarget EquipmentType equipmentType);
}