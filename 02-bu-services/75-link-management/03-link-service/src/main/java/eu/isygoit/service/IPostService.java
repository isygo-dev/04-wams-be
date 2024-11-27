package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.com.rest.service.IImageServiceMethods;
import eu.isygoit.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Post service.
 */
public interface IPostService extends ICrudServiceMethod<Long, Post>,
        IImageServiceMethods<Long, Post>,
        IFileServiceMethods<Long, Post> {

    /**
     * Find by domain and is blog true list.
     *
     * @param domain   the domain
     * @param pageable the pageable
     * @return the list
     */
    List<Post> findByDomainAndIsBlogTrue(String domain, Pageable pageable);

    /**
     * Find by is blog true list.
     *
     * @param pageable the pageable
     * @return the list
     */
    List<Post> findByIsBlogTrue(Pageable pageable);
}
