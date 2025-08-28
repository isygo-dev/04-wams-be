package eu.isygoit.repository;


import eu.isygoit.model.JobOfferTemplate;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAssignableRepository;

/**
 * The interface Job template repository.
 */
public interface JobOfferTemplateRepository extends JpaPagingAndSortingTenantAssignableRepository<JobOfferTemplate, Long> {

    /**
     * Delete job template by job offer id long.
     *
     * @param jobId the job id
     * @return the long
     */
    Long deleteJobTemplateByJobOffer_Id(Long jobId);
}
