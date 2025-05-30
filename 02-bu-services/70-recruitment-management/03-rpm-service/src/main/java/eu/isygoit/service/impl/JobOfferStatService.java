package eu.isygoit.service.impl;

import eu.isygoit.dto.common.RequestContextDto;
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
    public Long stat_GetConfirmedJobOffersCount(RequestContextDto requestContext) {
        return 181L;
    }

    /**
     * Returns total count of job offers for the domain.
     */
    public Long stat_GetJobOffersCount(RequestContextDto requestContext) {
        return jobOfferRepository.countByDomainIgnoreCase(requestContext.getSenderDomain());
    }

    /**
     * Returns count of active job offers for the domain.
     */
    public Long stat_GetActiveJobOffersCount(RequestContextDto requestContext) {
        return jobOfferRepository.countByDomainAndDeadLine(requestContext.getSenderDomain());
    }

    /**
     * Returns count of expired job offers for the domain.
     */
    public Long stat_GetExpiredJobOffersCount(RequestContextDto requestContext) {
        return jobOfferRepository.countByDomainAndExpiredDeadLine(requestContext.getSenderDomain());
    }
}