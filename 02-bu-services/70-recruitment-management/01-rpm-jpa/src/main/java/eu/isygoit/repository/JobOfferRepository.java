package eu.isygoit.repository;

import eu.isygoit.model.JobOffer;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Job offer repository.
 */
@Repository
public interface JobOfferRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<JobOffer, Long> {


    /**
     * Find job offers not assigned to resume list.
     *
     * @param resumeCode the resume code
     * @return the list
     */
    @Query(name = "JobOffer.findJobOffersNotAssignedToResume", nativeQuery = true)
    List<JobOffer> findJobOffersNotAssignedToResume(@Param("resumeCode") String resumeCode);

    /**
     * Count by tenant and dead line long.
     *
     * @param tenant the tenant
     * @return the long
     */
    @Query(name = "JobOffer.countActiveJobs", nativeQuery = true)
    Long countByTenantAndDeadLine(@Param("tenant") String tenant);

    /**
     * Count by tenant and expired dead line long.
     *
     * @param tenant the tenant
     * @return the long
     */
    @Query(name = "JobOffer.countExpiredJobs", nativeQuery = true)
    Long countByTenantAndExpiredDeadLine(@Param("tenant") String tenant);
}
