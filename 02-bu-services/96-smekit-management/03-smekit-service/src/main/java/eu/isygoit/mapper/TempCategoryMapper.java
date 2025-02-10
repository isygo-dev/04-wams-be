package eu.isygoit.mapper;

import eu.isygoit.dto.data.TempCategoryDto;
import eu.isygoit.model.TempCategory;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")

public interface TempCategoryMapper extends EntityMapper<TempCategory, TempCategoryDto> {
}
