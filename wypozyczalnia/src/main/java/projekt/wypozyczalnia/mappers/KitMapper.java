package projekt.wypozyczalnia.mappers;


import projekt.wypozyczalnia.data.dtos.KitDTO;
import projekt.wypozyczalnia.data.entities.Kit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KitMapper {

    KitDTO toDto(Kit kit);
    Kit fromDto(KitDTO kitDTO);
}
