package eu.isygoit.repository;

import eu.isygoit.annotation.IgnoreRepository;
import eu.isygoit.model.BlogTalk;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

/**
 * The interface Blog talk repository.
 */
@IgnoreRepository
public interface BlogTalkRepository extends CassandraRepository<BlogTalk, UUID> {

    /**
     * Find all by blog id slice.
     *
     * @param blogId   the blog id
     * @param pageable the pageable
     * @return the slice
     */
    @AllowFiltering
    Slice<BlogTalk> findAllByBlogId(Long blogId, Pageable pageable);

    /**
     * Find all by blog id list.
     *
     * @param blogId the blog id
     * @return the list
     */
    @AllowFiltering
    List<BlogTalk> findAllByBlogId(Long blogId);
}
