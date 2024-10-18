package eu.isygoit.mapper;

import eu.isygoit.dto.data.BlogDto;
import eu.isygoit.model.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Blog mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface BlogMapper extends EntityMapper<Blog, BlogDto> {
}
