package eu.isygoit.service.impl;

import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.repository.JobOfferRepository;
import org.springframework.stereotype.Service;

@Service
public class JobOfferStatService {

    private final JobOfferRepository jobOfferRepository;

    public JobOfferStatService(JobOfferRepository jobOfferRepository) {
        this.jobOfferRepository = jobOfferRepository;
    }

    /**
     * Returns the count of confirmed job offers. (Hardcoded here, replace with real logic)
     */
    public Long stat_GetConfirmedJobOffersCount(ContextRequestDto requestContext) {
        return 181L;
    }

    /**
     * Returns total count of job offers for the tenant.
     */
    public Long stat_GetJobOffersCount(ContextRequestDto requestContext) {
        return jobOfferRepository.countByTenantIgnoreCase(requestContext.getSenderTenant());
    }

    /**
     * Returns count of active job offers for the tenant.
     */
    public Long stat_GetActiveJobOffersCount(ContextRequestDto requestContext) {
        return jobOfferRepository.countByTenantAndDeadLine(requestContext.getSenderTenant());
    }

    /**
     * Returns count of expired job offers for the tenant.
     */
    public Long stat_GetExpiredJobOffersCount(ContextRequestDto requestContext) {
        return jobOfferRepository.countByTenantAndExpiredDeadLine(requestContext.getSenderTenant());
    }
}