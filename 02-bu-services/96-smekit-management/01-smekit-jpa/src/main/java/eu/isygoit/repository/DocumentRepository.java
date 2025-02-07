package eu.isygoit.repository;

import eu.isygoit.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository  extends JpaRepository<Document,Long> {
}
