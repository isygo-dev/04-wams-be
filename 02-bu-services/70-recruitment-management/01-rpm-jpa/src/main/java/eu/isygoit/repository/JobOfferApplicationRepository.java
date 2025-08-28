package eu.isygoit.repository;

import eu.isygoit.model.JobOfferApplication;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The interface Job offer application repository.
 */
public interface JobOfferApplicationRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<JobOfferApplication, Long> {
    /**
     * Cancel job application long.
     *
     * @param resumeId the resume id
     * @return the long
     */
    @Modifying
    @Query(name = "JobOfferApplication.cancel", nativeQuery = true)
    Long cancelJobApplication(@Param("resumeId") Long resumeId);

    /**
     * Count jobs by number of applications long.
     *
     * @param code   the code
     * @param tenant the tenant
     * @return the long
     */
    @Query(name = "JobOfferApplication.countNumberApplicationsByJob", nativeQuery = true)
    Long countJobsByNumberOfApplications(@Param("code") String code, @Param("tenant") String tenant);

    /**
     * Count ongoing global job application long.
     *
     * @param tenant the tenant
     * @return the long
     */
    @Query(name = "JobOfferApplication.countOngoingGlobalJobApplication", nativeQuery = true)
    Long countOngoingGlobalJobApplication(@Param("tenant") String tenant);

    /**
     * Count on going job application by resume long.
     *
     * @param tenant the tenant
     * @param code   the code
     * @return the long
     */
    @Query(name = "JobOfferApplication.countOnGoingApplicationsByResume", nativeQuery = true)
    Long countOnGoingJobApplicationByResume(@Param("tenant") String tenant, @Param("code") String code);

    /**
     * Count by resume code and tenant long.
     *
     * @param code   the code
     * @param tenant the tenant
     * @return the long
     */
    Long countByResumeCodeAndTenant(String code, String tenant);

}
