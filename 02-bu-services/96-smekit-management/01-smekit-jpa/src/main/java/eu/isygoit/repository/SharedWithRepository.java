package eu.isygoit.repository;

import eu.isygoit.model.SharedWith;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface SharedWithRepository  extends JpaPagingAndSortingRepository <SharedWith,Long> {
}
