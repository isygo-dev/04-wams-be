package eu.isygoit.repository;

import eu.isygoit.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository

public interface DocumentRepository  extends JpaPagingAndSortingCodifiableRepository<Document,Long> {
    Page<Document> findByCreatedBy(String createdBy, Pageable pageable);

}
