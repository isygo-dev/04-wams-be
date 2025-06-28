package eu.isygoit.repository;

import eu.isygoit.model.Document;
import eu.isygoit.model.SharedWith;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface SharedWithRepository  extends JpaPagingAndSortingRepository <SharedWith,Long> {

    List<SharedWith> findByDocument(Document document);
    List<SharedWith> findByUser(String user);

    Optional<SharedWith> findByDocumentAndUser(Document document, String user);
    boolean existsByDocumentAndUser(Document document, String user);

}
