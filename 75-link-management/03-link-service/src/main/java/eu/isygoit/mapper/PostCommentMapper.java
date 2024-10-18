package eu.isygoit.mapper;

import eu.isygoit.dto.data.PostCommentDto;
import eu.isygoit.model.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Post comment mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface PostCommentMapper extends EntityMapper<PostComment, PostCommentDto> {
}
