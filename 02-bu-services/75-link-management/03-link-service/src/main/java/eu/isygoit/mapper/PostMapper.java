package eu.isygoit.mapper;

import eu.isygoit.dto.data.PostDto;
import eu.isygoit.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Post mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface PostMapper extends EntityMapper<Post, PostDto> {

}
