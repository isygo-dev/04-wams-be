package eu.isygoit.repository;

import eu.isygoit.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


/**
 * The interface Post repository.
 */
@Repository
public interface PostRepository extends JpaPagingAndSortingSAASRepository<Post, Long> {

    /**
     * Find by domain and is blog true page.
     *
     * @param domain   the domain
     * @param pageable the pageable
     * @return the page
     */
    Page<Post> findByDomainAndIsBlogTrue(String domain, Pageable pageable);

    /**
     * Find by is blog true page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Post> findByIsBlogTrue(Pageable pageable);
}
