package eu.isygoit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.com.rest.service.IImageServiceMethods;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.ResumeGlobalStatDto;
import eu.isygoit.dto.data.ResumeStatDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.enums.IEnumResumeStatType;
import eu.isygoit.model.Resume;
import eu.isygoit.model.ResumeLinkedFile;
import eu.isygoit.model.ResumeShareInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * The interface Resume service.
 */
public interface IResumeService extends ICrudServiceMethod<Long, Resume>, IFileServiceMethods<Long, Resume>, IImageServiceMethods<Long, Resume> {

    /**
     * Share with accounts list.
     *
     * @param id          the id
     * @param resumeOwner the resume owner
     * @param account     the account
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    List<ResumeShareInfo> shareWithAccounts(Long id, String resumeOwner, List<AccountModelDto> account) throws JsonProcessingException;

    /**
     * Upload additional file list.
     *
     * @param id    the id
     * @param files the files
     * @return the list
     * @throws IOException the io exception
     */
    List<ResumeLinkedFile> uploadAdditionalFile(Long id, MultipartFile[] files) throws IOException;

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
     * Gets resume by account code.
     *
     * @param code the code
     * @return the resume by account code
     * @throws IOException the io exception
     */
    Resume getResumeByAccountCode(String code) throws IOException;

    /**
     * Gets resume account code.
     *
     * @param resumeCode the resume code
     * @return the resume account code
     */
    String getResumeAccountCode(String resumeCode);

    /**
     * Create resume for account resume.
     *
     * @param code   the code
     * @param resume the resume
     * @return the resume
     * @throws IOException the io exception
     */
    Resume createResumeForAccount(String code, Resume resume) throws IOException;

    /**
     * Find resume by code resume.
     *
     * @param code the code
     * @return the resume
     */
    Resume findResumeByCode(String code);

    /**
     * Gets global statistics.
     *
     * @param statType       the stat type
     * @param requestContext the request context
     * @return the global statistics
     */
    ResumeGlobalStatDto getGlobalStatistics(IEnumResumeStatType.Types statType, RequestContextDto requestContext);

    /**
     * Gets object statistics.
     *
     * @param code           the code
     * @param requestContext the request context
     * @return the object statistics
     */
    ResumeStatDto getObjectStatistics(String code, RequestContextDto requestContext);

    /**
     * Gets account code.
     *
     * @param resumeCode the resume code
     * @return the account code
     */
    String getAccountCode(String resumeCode);

    /**
     * Create account.
     *
     * @param resume the resume
     * @throws IOException the io exception
     */
    void createAccount(Resume resume) throws IOException;
}
