package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethods;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.com.rest.service.IImageServiceMethods;
import eu.isygoit.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Post service.
 */
public interface IPostService extends ICrudServiceMethods<Long, Post>,
        IImageServiceMethods<Long, Post>,
        IFileServiceMethods<Long, Post> {

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
