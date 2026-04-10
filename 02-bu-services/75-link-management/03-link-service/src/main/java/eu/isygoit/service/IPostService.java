package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceOperations;
import eu.isygoit.com.rest.service.media.IFileServiceOperations;
import eu.isygoit.com.rest.service.media.IImageServiceOperations;
import eu.isygoit.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Post service.
 */
public interface IPostService extends ICrudServiceOperations<Long, Post>,
        IImageServiceOperations<Long, Post>,
        IFileServiceOperations<Long, Post> {

    /**
     * Find by tenant and is blog true list.
     *
     * @param tenant   the tenant
     * @param pageable the pageable
     * @return the list
     */
    List<Post> findByTenantAndIsBlogTrue(String tenant, Pageable pageable);

    /**
     * Find by is blog true list.
     *
     * @param pageable the pageable
     * @return the list
     */
    List<Post> findByIsBlogTrue(Pageable pageable);
}
