package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.UserDTO;
import projekt.wypozyczalnia.data.entities.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleMapper.class})
public interface UserMapper {

    @Mappings({
            @Mapping(target = "userId", source = "userDTO.userId"),
            @Mapping(target = "username", source = "userDTO.username"),
            @Mapping(target = "password", source = "userDTO.password"),
            @Mapping(target = "firstname", source = "userDTO.firstname"),
            @Mapping(target = "lastname", source = "userDTO.lastname"),
            @Mapping(target = "email", source = "userDTO.email"),
            @Mapping(target = "phone", source = "userDTO.phone"),
            @Mapping(target = "role", source = "roleDto")
    })
    User toEntity(UserDTO userDTO);

    UserDTO toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDTO userDTO, @MappingTarget User user);
}