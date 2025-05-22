package eu.isygoit.repository;

import eu.isygoit.model.DocComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DocCommentsRepository  extends  JpaPagingAndSortingRepository<DocComment,Long> {
}
