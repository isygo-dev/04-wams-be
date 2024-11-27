package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.FileImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.exception.PostDeleteForbiddenException;
import eu.isygoit.exception.PostUpdateForbiddenException;
import eu.isygoit.helper.DateHelper;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Blog;
import eu.isygoit.model.Post;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.BlogRepository;
import eu.isygoit.repository.PostRepository;
import eu.isygoit.service.IPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The type Post service.
 */
@Slf4j
@Service
@Transactional
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = PostRepository.class)
public class PostService extends FileImageService<Long, Post, PostRepository>
        implements IPostService {

    private final AppProperties appProperties;

    @Autowired
    private BlogRepository blogRepository;

    /**
     * Instantiates a new Post service.
     *
     * @param appProperties the app properties
     */
    public PostService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    protected String getUploadDirectory() {
        return appProperties.getUploadDirectory();
    }

    @Override
    public void beforeDelete(Long id) {
        Post oldPost = this.findById(id);
        if (DateHelper.isInLastHours(oldPost.getCreateDate(), 24)) {
            throw new PostDeleteForbiddenException("With id " + id);
        }
        super.beforeDelete(id);
    }

    @Override
    public Post beforeUpdate(Post post) {
        Post oldPost = this.findById(post.getId());
        if (DateHelper.isInLastHours(oldPost.getCreateDate(), 24)) {
            oldPost.setTalk(post.getTalk());
            oldPost.setTitle(post.getTitle());
            oldPost.setImagePath(post.getImagePath());
            oldPost.setOriginalFileName(post.getOriginalFileName());
            oldPost.setExtension(post.getExtension());
            oldPost.setType(post.getType());
            if (CollectionUtils.isEmpty(oldPost.getTags())) {
                oldPost.setTags(new ArrayList<>() {{
                    add("Post");
                    add(post.getType());
                }});
            }
            return super.beforeUpdate(oldPost);
        } else {
            throw new PostUpdateForbiddenException("With id " + post.getId());
        }
    }

    @Override
    public Post afterCreate(Post post) {
        if (post.getIsBlog()) {
            blogRepository.save(Blog.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .description(post.getTalk())
                    .accountCode(post.getAccountCode())
                    .domain(post.getDomain())
                    .build());
        }
        return super.afterCreate(post);
    }

    @Override
    public Post afterUpdate(Post post) {
        if (post.getIsBlog()) {
            Blog blog = null;
            Optional<Blog> optional = blogRepository.findById(post.getId());
            if (optional.isPresent()) {
                blog = optional.get();
                blog.setTitle(post.getTitle());
                blog.setDescription(post.getTalk());
                blog.setAccountCode(post.getAccountCode());
            } else {
                blog = Blog.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .description(post.getTalk())
                        .accountCode(post.getAccountCode())
                        .build();
            }
            blogRepository.save(blog);
        }
        return super.afterUpdate(post);
    }

    @Override
    public Post beforeCreate(Post post) {
        if (CollectionUtils.isEmpty(post.getTags())) {
            post.setTags(new ArrayList<>() {{
                add("Post");
                add(post.getType());
            }});
        }
        return super.beforeCreate(post);
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Post.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("PST")
                .valueLength(6L)
                .value(1L)
                .build();
    }

    @Override
    public List<Post> findByDomainAndIsBlogTrue(String domain, Pageable pageable) {
        Page<Post> posts = repository().findByDomainAndIsBlogTrue(domain, pageable);
        if (!posts.isEmpty()) {
            return posts.toList();
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Post> findByIsBlogTrue(Pageable pageable) {
        Page<Post> posts = repository().findByIsBlogTrue(pageable);
        if (!posts.isEmpty()) {
            return posts.toList();
        }
        return Collections.EMPTY_LIST;
    }
}
