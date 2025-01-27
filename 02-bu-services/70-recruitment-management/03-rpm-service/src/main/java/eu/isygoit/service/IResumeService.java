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
import eu.isygoit.model.ResumeShareInfo;

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
     * @param accounts    the account
     * @return the list
     * @throws JsonProcessingException the json processing exception
     */
    List<ResumeShareInfo> shareWithAccounts(Long id, String resumeOwner, List<AccountModelDto> accounts) throws JsonProcessingException;

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
