package eu.isygoit.repository;

import eu.isygoit.annotation.IgnoreRepository;
import eu.isygoit.model.Blog;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;


/**
 * The interface Blog repository.
 */
@IgnoreRepository
public interface BlogRepository extends CassandraRepository<Blog, Long> {
}
