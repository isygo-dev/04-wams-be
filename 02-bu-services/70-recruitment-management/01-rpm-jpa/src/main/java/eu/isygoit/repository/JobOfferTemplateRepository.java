package eu.isygoit.repository;


import eu.isygoit.model.JobOfferTemplate;

/**
 * The interface Job template repository.
 */
public interface JobOfferTemplateRepository extends JpaPagingAndSortingSAASRepository<JobOfferTemplate, Long> {

    /**
     * Delete job template by job offer id long.
     *
     * @param jobId the job id
     * @return the long
     */
    Long deleteJobTemplateByJobOffer_Id(Long jobId);
}
