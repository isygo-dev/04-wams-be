package eu.isygoit.repository;

import eu.isygoit.model.JobOfferApplication;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The interface Job offer application repository.
 */
public interface JobOfferApplicationRepository extends JpaPagingAndSortingSAASCodifiableRepository<JobOfferApplication, Long> {
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
     * @param domain the domain
     * @return the long
     */
    @Query(name = "JobOfferApplication.countNumberApplicationsByJob", nativeQuery = true)
    Long countJobsByNumberOfApplications(@Param("code") String code, @Param("domain") String domain);

    /**
     * Count ongoing global job application long.
     *
     * @param domain the domain
     * @return the long
     */
    @Query(name = "JobOfferApplication.countOngoingGlobalJobApplication", nativeQuery = true)
    Long countOngoingGlobalJobApplication(@Param("domain") String domain);

    /**
     * Count on going job application by resume long.
     *
     * @param domain the domain
     * @param code   the code
     * @return the long
     */
    @Query(name = "JobOfferApplication.countOnGoingApplicationsByResume", nativeQuery = true)
    Long countOnGoingJobApplicationByResume(@Param("domain") String domain, @Param("code") String code);

    /**
     * Count by resume code and domain long.
     *
     * @param code   the code
     * @param domain the domain
     * @return the long
     */
    Long countByResumeCodeAndDomain(String code, String domain);

}
