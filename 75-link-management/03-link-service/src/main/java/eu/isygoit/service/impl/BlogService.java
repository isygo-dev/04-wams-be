package eu.isygoit.service.impl;

import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.cassandra.CassandraCrudService;
import eu.isygoit.model.Blog;
import eu.isygoit.repository.BlogRepository;
import eu.isygoit.service.IBlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Blog service.
 */
@Slf4j
@Service
@Transactional
@SrvRepo(value = BlogRepository.class)
public class BlogService extends CassandraCrudService<Long, Blog, BlogRepository> implements IBlogService {

}
