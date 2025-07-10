package eu.isygoit.mapper;

import eu.isygoit.dto.data.CategoryDto;
import eu.isygoit.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public abstract class CategoryMapper implements EntityMapper<Category, CategoryDto> {
}
