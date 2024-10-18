package eu.isygoit.repository;

import eu.isygoit.model.Vacation;
import org.springframework.stereotype.Repository;


/**
 * The interface Vacation repository.
 */
@Repository
public interface VacationRepository extends JpaPagingAndSortingRepository<Vacation, Long> {

}
