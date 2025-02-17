package eu.isygoit.repository;

import eu.isygoit.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface DocumentRepository  extends JpaPagingAndSortingRepository<Document,Long> {
}
