package eu.isygoit.repository;

import eu.isygoit.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface AuthorRepository  extends JpaPagingAndSortingCodifiableRepository<Author,Long> {
}
