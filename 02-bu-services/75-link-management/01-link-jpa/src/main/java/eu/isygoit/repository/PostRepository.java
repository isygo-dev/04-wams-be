package eu.isygoit.repository;

import eu.isygoit.model.Post;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


/**
 * The interface Post repository.
 */
@Repository
public interface PostRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<Post, Long> {

    /**
     * Find by tenant and is blog true page.
     *
     * @param tenant   the tenant
     * @param pageable the pageable
     * @return the page
     */
    Page<Post> findByTenantAndIsBlogTrue(String tenant, Pageable pageable);

    /**
     * Find by is blog true page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Post> findByIsBlogTrue(Pageable pageable);
}
