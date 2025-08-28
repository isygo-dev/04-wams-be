package eu.isygoit.repository;

import eu.isygoit.model.Vacation;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Vacation repository.
 */
@Repository
public interface VacationRepository extends JpaPagingAndSortingRepository<Vacation, Long> {

}
