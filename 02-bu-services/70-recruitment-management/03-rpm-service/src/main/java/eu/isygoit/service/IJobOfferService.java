package eu.isygoit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferGlobalStatDto;
import eu.isygoit.dto.data.JobOfferStatDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.model.JobOffer;
import eu.isygoit.model.JobOfferShareInfo;

import java.util.List;

/**
 * The interface Job offer service.
 */
public interface IJobOfferService extends ICrudServiceMethod<Long, JobOffer> {


    /**
     * Find job offers not assigned to resume list.
     *
     * @param resumeCode the resume code
     * @return the list
     */
    List<JobOffer> findJobOffersNotAssignedToResume(String resumeCode);


    List<JobOfferShareInfo> shareJob(Long id, String jobOwner, List<AccountModelDto> accounts) throws JsonProcessingException;

    /**
     * Gets global statistics.
     *
     * @param statType       the stat type
     * @param requestContext the request context
     * @return the global statistics
     */
    JobOfferGlobalStatDto getGlobalStatistics(IEnumSharedStatType.Types statType, RequestContextDto requestContext);

    /**
     * Gets object statistics.
     *
     * @param code           the code
     * @param requestContext the request context
     * @return the object statistics
     */
    JobOfferStatDto getObjectStatistics(String code, RequestContextDto requestContext);
}
