package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.RoleDTO;
import projekt.wypozyczalnia.data.entities.Role;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    @Mapping(target = "roleId", source = "roleDTO.roleId")
    @Mapping(target = "type", source = "roleDTO.type")
    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDto(Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Role partialUpdate(RoleDTO roleDTO, @MappingTarget Role role);
}