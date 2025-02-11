package eu.isygoit.repository;

import eu.isygoit.model.DocComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocCommentsRepository  extends  JpaPagingAndSortingRepository<DocComment,Long> {
}
