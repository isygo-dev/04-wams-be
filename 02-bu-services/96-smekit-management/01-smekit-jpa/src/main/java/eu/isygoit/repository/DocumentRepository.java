package eu.isygoit.repository;

import eu.isygoit.model.Document;
import org.springframework.stereotype.Repository;

@Repository

public interface DocumentRepository extends JpaPagingAndSortingCodeAssingnableRepository<Document, Long> {
}
