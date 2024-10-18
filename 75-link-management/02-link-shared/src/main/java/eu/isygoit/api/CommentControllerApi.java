package eu.isygoit.api;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.dto.data.PostCommentDto;

/**
 * The interface Comment controller api.
 */
public interface CommentControllerApi extends IMappedCrudApi<Long, PostCommentDto, PostCommentDto> {
}
