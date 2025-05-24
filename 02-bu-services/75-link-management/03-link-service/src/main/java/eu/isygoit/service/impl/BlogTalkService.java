package eu.isygoit.service.impl;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import eu.isygoit.annotation.ServRepo;
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
import java.util.Optional;
import java.util.UUID;

/**
 * The type Blog talk service.
 */
@Slf4j
@Service
@Transactional
@ServRepo(value = BlogTalkRepository.class)
public class BlogTalkService extends CassandraCrudService<UUID, BlogTalk, BlogTalkRepository>
        implements IBlogTalkService {

    @Override
    public List<BlogTalk> findByBlogId(Long blogId) {
        List<BlogTalk> talks = repository().findAllByBlogId(blogId);
        if (talks.isEmpty()) {
            return Collections.emptyList();
        }
        return this.afterFindAll(talks);
    }

    @Override
    public List<BlogTalk> findByBlogId(Long blogId, Integer page, Integer size) throws NotSupportedException {
        Slice<BlogTalk> talks = repository().findAllByBlogId(blogId, PageRequest.of(page, size));
        if (talks.isEmpty()) {
            return Collections.emptyList();
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
        this.findById(id).ifPresent(blogTalk -> {
            if (DateHelper.occurredInLastXHours(blogTalk.getCreateDate(), 24)) {
                throw new BlogTalkUpdateForbiddenException("With id " + id);
            }
        });

        super.beforeDelete(id);
    }

    @Override
    public BlogTalk beforeUpdate(BlogTalk blogTalk) {
        Optional<BlogTalk> optional = this.findById(blogTalk.getId());
        optional.ifPresent(oldblogTalk -> {
            if (DateHelper.occurredInLastXHours(oldblogTalk.getCreateDate(), 24)) {
                optional.get().setText(blogTalk.getText());
            }
        });

        return super.beforeUpdate(optional.get());
    }
}
