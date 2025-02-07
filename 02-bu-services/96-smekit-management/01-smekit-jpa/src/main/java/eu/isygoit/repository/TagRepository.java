package eu.isygoit.repository;

import eu.isygoit.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository  extends JpaRepository<Tag,Long> {
}
