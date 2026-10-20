package projekt.wypozyczalnia.mappers;

import org.mapstruct.*;
import projekt.wypozyczalnia.data.dtos.SkillLevelDTO;
import projekt.wypozyczalnia.data.entities.SkillLevel;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SkillLevelMapper {

    @Mappings({
            @Mapping(target = "skillLevelId", source = "skillLevelId"),
            @Mapping(target = "name", source = "name")
    })
    SkillLevelDTO toDto(SkillLevel skillLevel);

    @Mappings({
            @Mapping(target = "skillLevelId", source = "skillLevelId"),
            @Mapping(target = "name", source = "name")
    })
    SkillLevel toEntity(SkillLevelDTO skillLevelDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SkillLevel partialUpdate(SkillLevelDTO skillLevelDTO, @MappingTarget SkillLevel skillLevel);
}