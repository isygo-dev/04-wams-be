package eu.isygoit.repository;

import eu.isygoit.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface TagRepository  extends JpaPagingAndSortingRepository <Tag,Long> {
}
