package eu.isygoit.repository;


import eu.isygoit.model.ResumeDetails;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Resume details repository.
 */
@Repository
public interface ResumeDetailsRepository extends JpaPagingAndSortingRepository<ResumeDetails, Long> {
}
