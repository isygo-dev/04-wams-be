package eu.isygoit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferGlobalStatDto;
import eu.isygoit.dto.data.JobOfferStatDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.model.JobOffer;
import eu.isygoit.model.JobOfferLinkedFile;
import eu.isygoit.model.JobOfferShareInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    /**
     * Upload additional file list.
     *
     * @param parentId the parent id
     * @param files    the files
     * @return the list
     * @throws IOException the io exception
     */
    List<JobOfferLinkedFile> uploadAdditionalFile(Long parentId, MultipartFile[] files) throws IOException;

    /**
     * Delete additional file boolean.
     *
     * @param parentId the parent id
     * @param fileId   the file id
     * @return the boolean
     * @throws IOException the io exception
     */
    boolean deleteAdditionalFile(Long parentId, Long fileId) throws IOException;

    /**
     * Share job list.
     *
     * @param id       the id
     * @param jobOwner the job owner
     * @param account  the account
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    List<JobOfferShareInfo> shareJob(Long id, String jobOwner, List<AccountModelDto> account) throws JsonProcessingException;

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
