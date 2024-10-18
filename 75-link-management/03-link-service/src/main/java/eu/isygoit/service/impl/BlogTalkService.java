package eu.isygoit.service.impl;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.cassandra.CassandraCrudService;
import eu.isygoit.exception.BlogTalkUpdateForbiddenException;
import eu.isygoit.helper.DateHelper;
import eu.isygoit.model.BlogTalk;
import eu.isygoit.repository.BlogTalkRepository;
import eu.isygoit.service.IBlogTalkService;
import jakarta.transaction.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The type Blog talk service.
 */
@Slf4j
@Service
@Transactional
@SrvRepo(value = BlogTalkRepository.class)
public class BlogTalkService extends CassandraCrudService<UUID, BlogTalk, BlogTalkRepository>
        implements IBlogTalkService {

    @Override
    public List<BlogTalk> findByBlogId(Long blogId) {
        List<BlogTalk> talks = repository().findAllByBlogId(blogId);
        if (talks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return this.afterFindAll(talks);
    }

    @Override
    public List<BlogTalk> findByBlogId(Long blogId, Integer page, Integer size) throws NotSupportedException {
        Slice<BlogTalk> talks = repository().findAllByBlogId(blogId, PageRequest.of(page, size));
        if (talks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return this.afterFindAll(talks.getContent());
    }

    @Override
    public BlogTalk beforeCreate(BlogTalk blogTalk) {
        blogTalk.setId(Uuids.timeBased());
        return super.beforeCreate(blogTalk);
    }

    @Override
    public void beforeDelete(UUID id) {
        BlogTalk oldblogTalk = this.findById(id);
        if (DateHelper.isInLastHours(oldblogTalk.getCreateDate(), 24)) {
            throw new BlogTalkUpdateForbiddenException("With id " + id);
        }
        super.beforeDelete(id);
    }

    @Override
    public BlogTalk beforeUpdate(BlogTalk blogTalk) {
        BlogTalk oldblogTalk = this.findById(blogTalk.getId());
        if (DateHelper.isInLastHours(oldblogTalk.getCreateDate(), 24)) {
            oldblogTalk.setText(blogTalk.getText());
            return super.beforeUpdate(oldblogTalk);
        } else {
            throw new BlogTalkUpdateForbiddenException("With id " + blogTalk.getId());
        }
    }
}
