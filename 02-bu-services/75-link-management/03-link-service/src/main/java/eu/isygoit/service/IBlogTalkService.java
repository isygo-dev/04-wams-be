package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.model.BlogTalk;
import jakarta.transaction.NotSupportedException;

import java.util.List;
import java.util.UUID;

/**
 * The interface Blog talk service.
 */
public interface IBlogTalkService extends ICrudServiceMethod<UUID, BlogTalk> {

    /**
     * Find by blog id list.
     *
     * @param blogId the blog id
     * @return the list
     */
    List<BlogTalk> findByBlogId(Long blogId);

    /**
     * Find by blog id list.
     *
     * @param blogId the blog id
     * @param page   the page
     * @param size   the size
     * @return the list
     * @throws NotSupportedException the not supported exception
     */
    List<BlogTalk> findByBlogId(Long blogId, Integer page, Integer size) throws NotSupportedException;
}
