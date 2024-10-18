package eu.isygoit.repository;

import eu.isygoit.model.JobOffer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Job offer repository.
 */
@Repository
public interface JobOfferRepository extends JpaPagingAndSortingSAASCodifiableRepository<JobOffer, Long> {


    /**
     * Find job offers not assigned to resume list.
     *
     * @param resumeCode the resume code
     * @return the list
     */
    @Query(name = "JobOffer.findJobOffersNotAssignedToResume", nativeQuery = true)
    List<JobOffer> findJobOffersNotAssignedToResume(@Param("resumeCode") String resumeCode);

    /**
     * Count by domain and dead line long.
     *
     * @param domain the domain
     * @return the long
     */
    @Query(name = "JobOffer.countActiveJobs", nativeQuery = true)
    long countByDomainAndDeadLine(@Param("domain") String domain);

    /**
     * Count by domain and expired dead line long.
     *
     * @param domain the domain
     * @return the long
     */
    @Query(name = "JobOffer.countExpiredJobs", nativeQuery = true)
    long countByDomainAndExpiredDeadLine(@Param("domain") String domain);
}
