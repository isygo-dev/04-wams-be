package eu.isygoit.api;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.dto.data.PostDto;

/**
 * The interface Post controller api.
 */
public interface PostControllerApi extends IMappedCrudApi<Long, PostDto, PostDto> {

}
