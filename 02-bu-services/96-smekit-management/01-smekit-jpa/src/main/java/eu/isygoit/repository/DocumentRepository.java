package eu.isygoit.repository;

import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.model.Category;
import eu.isygoit.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DocumentRepository  extends JpaPagingAndSortingCodifiableRepository<Document,Long> {
}
