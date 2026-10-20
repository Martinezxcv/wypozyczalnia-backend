package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.AddressDTO;
import projekt.wypozyczalnia.data.entities.Address;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {


    Address toEntity(AddressDTO addressDTO);

    @Mapping(target = "addressId", source = "addressId")
    @Mapping(target = "isDefault", source = "default")
    AddressDTO toDto(Address address);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Address partialUpdate(AddressDTO addressDTO, @MappingTarget Address address);
}