package eu.isygoit.repository;

import eu.isygoit.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface TagRepository  extends JpaPagingAndSortingRepository <Tag,Long> {
    Optional<Tag> findByTagName(String tagName);


}
