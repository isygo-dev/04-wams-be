package eu.isygoit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.async.kafka.KafkaRegisterAccountProducer;
import eu.isygoit.com.camel.repository.ICamelRepository;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.service.impl.FileImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.AppParameterConstants;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.LinkedFileRequestDto;
import eu.isygoit.dto.common.LinkedFileResponseDto;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.*;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.dto.request.NewAccountDto;
import eu.isygoit.enums.IEnumAccountOrigin;
import eu.isygoit.enums.IEnumMsgTemplateName;
import eu.isygoit.enums.IEnumResumeStatType;
import eu.isygoit.exception.ResumeNotFoundException;
import eu.isygoit.exception.StatisticTypeNotSupportedException;
import eu.isygoit.model.*;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.ims.ImAccountService;
import eu.isygoit.remote.ims.ImsAppParameterService;
import eu.isygoit.remote.ims.ImsDomainService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.remote.quiz.QuizCandidateQuizService;
import eu.isygoit.repository.AssoAccountResumeRepository;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.ResumeLinkedFileRepository;
import eu.isygoit.repository.ResumeRepository;
import eu.isygoit.service.IMsgService;
import eu.isygoit.service.IResumeService;
import eu.isygoit.types.EmailSubjects;
import eu.isygoit.types.MsgTemplateVariables;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The type Resume service.
 */
@Slf4j
@Service
@Transactional
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = ResumeRepository.class)
public class ResumeService extends FileImageService<Long, Resume, ResumeRepository>
        implements IResumeService {

    private final AppProperties appProperties;

    @Autowired
    private DmsLinkedFileService dmsLinkedFileService;
    @Autowired
    private AssoAccountResumeRepository assoAccountResumeRepository;
    @Autowired
    private IMsgService msgService;
    @Autowired
    private ICamelRepository camelRepository;
    @Autowired
    private ResumeLinkedFileRepository resumeLinkedFileRepository;
    @Autowired
    private ImsAppParameterService imsAppParameterService;
    @Autowired
    private ImsDomainService imsDomainService;
    @Autowired
    private JobOfferApplicationRepository jobApplicationRepository;
    @Autowired
    private KafkaRegisterAccountProducer kafkaRegisterAccountProducer;
    @Autowired
    private QuizCandidateQuizService quizCandidateQuizService;
    @Autowired
    private ImAccountService imAccountService;


    /**
     * Instantiates a new Resume service.
     *
     * @param appProperties the app properties
     */
    public ResumeService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Resume.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("RES")
                .valueLength(6L)
                .value(1L)
                .build();
    }

    @Override
    public Resume beforeUpload(String domain, Resume resume, MultipartFile file) throws IOException {
        camelRepository.asyncSendBody(ICamelRepository.parse_resume_queue, ResumeParseDto.builder()
                .domain(resume.getDomain())
                .code(resume.getCode())
                .file(file)
                .build());
        return resume;
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public List<ResumeShareInfo> shareWithAccounts(Long id, String resumeOwner, List<AccountModelDto> account) throws JsonProcessingException {
        Resume resume = findById(id);
        List<ResumeShareInfo> shareInfos = new ArrayList<>();
        if (resume != null) {
            for (AccountModelDto acc : account) {
                boolean exists = false;
                ResumeShareInfo existingShareInfo = null;

                for (ResumeShareInfo shareInfo : resume.getResumeShareInfos()) {
                    if (shareInfo.getSharedWith().equals(acc.getCode())) {
                        exists = true;
                        existingShareInfo = shareInfo;
                        break;
                    }
                }

                if (!exists) {
                    ResumeShareInfo shareInfo = new ResumeShareInfo();
                    shareInfo.setSharedWith(acc.getCode());
                    shareInfos.add(shareInfo);
                    shareResumeNotification(resume, resumeOwner, acc);
                } else {
                    shareInfos.add(existingShareInfo);
                }
            }
            resume.getResumeShareInfos().clear();
            resume.getResumeShareInfos().addAll(shareInfos);
            return repository().save(resume).getResumeShareInfos();
        } else {
            // Handle the case where the Resume with the given ID doesn't exist
            throw new NotFoundException("Resume not found with ID: " + id);
        }
    }

    @Override
    public List<ResumeLinkedFile> uploadAdditionalFile(Long id, MultipartFile[] files) throws IOException {
        Resume resume = findById(id);

        // int crc16 = CRC16.calculate(buffer);
        //int crc32 = CRC32.calculate(buffer);
        if (resume != null) {
            for (MultipartFile file : files) {
                try {
                    ResponseEntity<LinkedFileResponseDto> result = dmsLinkedFileService.upload(//RequestContextDto.builder().build(),
                            LinkedFileRequestDto.builder()
                                    .domain(resume.getDomain())
                                    .path(File.separator + "resume" + File.separator + "additional")
                                    .tags(resume.getTags())
                                    .categoryNames(Arrays.asList("Resume"))
                                    .file(file)
                                    .build());
                    if (result.getStatusCode().is2xxSuccessful() && result.hasBody()) {
                        ResumeLinkedFile resumeLinkedFile = ResumeLinkedFile.builder()
                                .code(result.getBody().getCode())   //NOSONAR
                                .originalFileName(file.getOriginalFilename())
                                .extension(FilenameUtils.getExtension(file.getOriginalFilename()))
                                .crc16(254147)
                                .crc32(365214)
                                .size(file.getSize())
                                .path("/resume/additional")
                                .mimetype(file.getContentType())
                                .version(1L)
                                .build();
                        if (CollectionUtils.isEmpty(resume.getAdditionalFiles())) {
                            resume.setAdditionalFiles(new ArrayList<>());
                        }
                        resume.getAdditionalFiles().add(resumeLinkedFile);
                        resume = this.update(resume);
                    }
                } catch (Exception e) {
                    log.error("Remote feign call failed : ", e);
                    //throw new RemoteCallFailedException(e);
                }
            }
            return resume.getAdditionalFiles();
        } else {
            throw new ResumeNotFoundException("with id: " + id);
        }
    }

    @Override
    public boolean deleteAdditionalFile(Long parentId, Long fileId) throws IOException {
        Resume resume = findById(parentId);
        if (resume != null) {
            ResumeLinkedFile resumeLinkedFile = resume.getAdditionalFiles().stream()
                    .filter((ResumeLinkedFile item) -> item.getId().equals(fileId)).findAny()
                    .orElse(null);
            if (resumeLinkedFile != null) {
                resume.getAdditionalFiles().removeIf(elm -> elm.getId().equals(fileId));
                dmsLinkedFileService.deleteFile(RequestContextDto.builder().build(), resume.getDomain(), resumeLinkedFile.getCode());
                this.update(resume);
                resumeLinkedFileRepository.deleteById(resumeLinkedFile.getId());
                return true;
            } else {
                throw new FileNotFoundException("with original File name: " + fileId);
            }
        } else {
            throw new ResumeNotFoundException("with id: " + parentId);
        }
    }

    private void shareResumeNotification(Resume resume, String resumeOwner, AccountModelDto account) throws JsonProcessingException {
        //Send email/notification to the board watchers
        String resumeUrl = "https://localhost:4004/apps/resumes/view/";
        try {
            ResponseEntity<String> result = imsAppParameterService.getValueByDomainAndName(RequestContextDto.builder().build(),
                    resume.getDomain(), AppParameterConstants.RESUME_VIEW_URL, true, "https://localhost:4004/apps/resumes/view/");
            if (result.hasBody() && StringUtils.hasText(result.getBody())) {
                resumeUrl = result.getBody() + resume.getId();
            }
        } catch (Exception e) {
            log.error("Remote feign call failed : ", e);
            //throw new RemoteCallFailedException(e);
        }

        MailMessageDto mailMessageDto = MailMessageDto.builder()
                .domain(resume.getDomain())
                .subject(EmailSubjects.SHARED_RESUME_EMAIL_SUBJECT)
                .toAddr(account.getEmail())
                .templateName(IEnumMsgTemplateName.Types.RESUME_SHARED_TEMPLATE)
                .sent(true)
                .build();

        ResponseEntity<DomainDto> resultDomain = imsDomainService.getByName(RequestContextDto.builder().build());
        DomainDto domain = null;
        if (resultDomain.hasBody() && resultDomain.getBody() != null) {
            domain = resultDomain.getBody();
        }
        mailMessageDto.setVariables(MailMessageDto.getVariablesAsString(Map.of(
                //Common vars
                MsgTemplateVariables.V_USER_NAME, account.getCode(),
                MsgTemplateVariables.V_FULLNAME, account.getFullName(),
                MsgTemplateVariables.V_DOMAIN_NAME, resume.getDomain(),
                //Specific vars
                MsgTemplateVariables.V_CONDIDATE_FULLNAME, resume.getFullName(),
                MsgTemplateVariables.V_SHARED_BY, resumeOwner,
                MsgTemplateVariables.V_RESUME_VIEW_URL, resumeUrl)));

        msgService.sendMessage(resume.getDomain(), mailMessageDto, appProperties.isSendAsyncEmail());
    }


    public Resume getResumeByAccountCode(String accountCode) {
        Optional<AssoAccountResume> optional = assoAccountResumeRepository.findByAccountCodeIgnoreCase(accountCode);
        if (optional.isPresent()) {
            completeSkills(optional.get().getResume(), accountCode);
            return optional.get().getResume();
        }

        return null;
    }

    public String getResumeAccountCode(String resumeCode) {
        Optional<AssoAccountResume> optional = assoAccountResumeRepository.findByResume_Code(resumeCode);
        if (optional.isPresent()) {
            return optional.get().getAccountCode();
        }

        return null;
    }

    @Override
    public Resume createResumeForAccount(String accountCode, Resume resume) throws IOException {
        //Check if Resume is already created
        AssoAccountResume assoAccountResume = null;
        Optional<AssoAccountResume> optional = assoAccountResumeRepository.findByAccountCodeIgnoreCase(accountCode);
        if (!optional.isPresent()) {
            resume = this.create(resume);
            assoAccountResume = assoAccountResumeRepository.save(AssoAccountResume.builder()
                    .accountCode(accountCode)
                    .resume(resume)
                    .build());
        } else {
            assoAccountResume = optional.get();
            if (assoAccountResume.getResume() != null) {
                resume.setId(assoAccountResume.getResume().getId());
                resume = this.update(resume);
            } else {
                log.warn("Associated resume for account {} not found" + accountCode);
                resume = this.create(resume);
                assoAccountResume.setResume(resume);
                assoAccountResume = assoAccountResumeRepository.save(assoAccountResume);
            }
        }

        return assoAccountResume.getResume();
    }

    @Override
    public Resume findResumeByCode(String code) {
        Optional<Resume> optional = repository().findByCodeIgnoreCase(code);
        if (optional.isPresent()) {
            Optional<AssoAccountResume> accountResume = assoAccountResumeRepository.findByResume_Code(code);
            if (accountResume.isPresent()) {
                completeSkills(optional.get(), accountResume.get().getAccountCode());
            }
            return optional.get();
        } else {
            throw new NotFoundException("Resume not found with CODE: " + code);
        }
    }

    @Override
    public ResumeGlobalStatDto getGlobalStatistics(IEnumResumeStatType.Types statType, RequestContextDto requestContext) {
        ResumeGlobalStatDto.ResumeGlobalStatDtoBuilder builder = ResumeGlobalStatDto.builder();
        switch (statType) {
            case TOTAL_COUNT:
                builder.totalCount(stat_GetResumesCount(requestContext));
                break;
            case UPLOADED_BY_ME_COUNT:
                builder.uploadedByMeCount(stat_GetUploadedByMeResumesCount(requestContext));
                break;
            case CONFIRMED_COUNT:
                builder.confirmedCount(stat_GetConfirmedResumesCount(requestContext));
                break;
            case COMPLETED_COUNT:
                builder.completedCount(stat_GetCompletedResumesCount(requestContext));
                break;
            case INTERVIEWED_COUNT:
                builder.interviewedCount(stat_GetInterviewdResumesCount(requestContext));
                break;
            default:
                throw new StatisticTypeNotSupportedException(statType.name());
        }

        return builder.build();
    }

    private Long stat_GetCompletedResumesCount(RequestContextDto requestContext) {
        return 58L;
    }

    private Long stat_GetResumesCount(RequestContextDto requestContext) {
        if (DomainConstants.SUPER_DOMAIN_NAME.equals(requestContext.getSenderDomain())) {
            return repository().count();
        } else {
            return repository().countByDomainIgnoreCase(requestContext.getSenderDomain());
        }
    }

    private Long stat_GetUploadedByMeResumesCount(RequestContextDto requestContext) {
        return repository().countByCreatedBy(requestContext.getCreatedByString());
    }

    private Long stat_GetConfirmedResumesCount(RequestContextDto requestContext) {
        ResponseEntity<Long> responseEntity = imAccountService.getConfirmedResumeAccountsCount(requestContext);
        return responseEntity.getBody();
    }

    private Long stat_GetInterviewdResumesCount(RequestContextDto requestContext) {
        return jobApplicationRepository.countOngoingGlobalJobApplication(requestContext.getSenderDomain());
    }

    @Override
    public ResumeStatDto getObjectStatistics(String code, RequestContextDto requestContext) {
        return ResumeStatDto.builder()
                .completion(75L)
                .realizedTestsCount(stat_GetInterviewedResumesCount(code))
                .applicationsCount(stat_GetJobApplicationResumesCount(code, requestContext))
                .ongoingApplicationsCount(stat_GetOngoingJobApplicationResumesCount(code, requestContext))
                .build();
    }

    @Override
    public String getAccountCode(String resumeCode) {
        Optional<AssoAccountResume> assoAccountResume = assoAccountResumeRepository.findByResume_Code(resumeCode);
        return assoAccountResume.isPresent() ? assoAccountResume.get().getAccountCode() : null;
    }

    private Long stat_GetInterviewedResumesCount(String code) {
        Optional<AssoAccountResume> assoAccountResume = assoAccountResumeRepository.findByResume_Code(code);
        if (assoAccountResume.isPresent()) {
            ResponseEntity<Long> responseEntity = quizCandidateQuizService.getCountRealizedTestByAccount(assoAccountResume.get().getAccountCode());
            return responseEntity.getBody();
        }
        return 0L;
    }

    private Long stat_GetJobApplicationResumesCount(String code, RequestContextDto requestContext) {
        return jobApplicationRepository.countByResumeCodeAndDomain(code, requestContext.getSenderDomain());
    }

    private Long stat_GetOngoingJobApplicationResumesCount(String code, RequestContextDto requestContext) {
        return jobApplicationRepository.countOnGoingJobApplicationByResume(requestContext.getSenderDomain(), code);
    }

    @Override
    public void afterDelete(Long id) {
        this.jobApplicationRepository.cancelJobApplication(id);
        super.afterDelete(id);
    }

    @Override
    public Resume afterCreate(Resume resume) {
        try {
            //Create linked account
            if (resume.getIsLinkedToUser()) {
                createAccount(resume);
            }
        } catch (IOException e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
        }
        return super.afterCreate(resume);
    }

    public void createAccount(Resume resume) throws IOException {
        kafkaRegisterAccountProducer.sendMessage(NewAccountDto.builder()
                .origin(new StringBuilder(IEnumAccountOrigin.Types.RESUME.name()).append("-").append(resume.getCode()).toString())
                .domain(resume.getDomain())
                .email(resume.getEmail())
                .firstName(resume.getFirstName())
                .lastName(resume.getLastName())
                .phoneNumber(resume.getPhone())
                .build());
    }

    @Override
    public Resume afterFindById(Resume resume) {
        if (resume.getDetails() != null && !CollectionUtils.isEmpty(resume.getDetails().getSkills())) {
            Optional<AssoAccountResume> accountResume = assoAccountResumeRepository.findByResume_Code(resume.getCode());
            if (accountResume.isPresent()) {
                completeSkills(resume, accountResume.get().getAccountCode());
            }
        }
        return super.afterFindById(resume);
    }


    private void completeSkills(Resume resume, String accountCode) {
        resume.getDetails().getSkills().forEach(resumeSkills -> {
            try {
                ResponseEntity<List<QuizReportDto>> result = quizCandidateQuizService.getByCandidateAndTags(RequestContextDto.builder().build(),
                        accountCode,
                        Arrays.asList(resumeSkills.getName())
                );
                if (result.getStatusCode().is2xxSuccessful() && result.hasBody()) {
                    resumeSkills.setScore(result.getBody().stream()
                            .mapToDouble(quizReportDto -> (quizReportDto.getScale() != 0 ? (quizReportDto.getScore() / quizReportDto.getScale()) : 0) * 100).average()
                            .orElse(Double.NaN));
                }
            } catch (Exception e) {
                log.error("Remote feign call failed : ", e);
                //throw new RemoteCallFailedException(e);
            }
        });
    }

    @Override
    public Resume beforeUpdate(Resume resume) {
        Resume oldResume = this.findById(resume.getId());
        if (CollectionUtils.isEmpty(oldResume.getTags())) {
            oldResume.setTags(Arrays.asList("Resume" /*, resume.getType()*/));
        }
        return super.beforeUpdate(oldResume);
    }

    @Override
    public Resume beforeCreate(Resume resume) {
        if (resume.getDetails() == null) {
            resume.setDetails(ResumeDetails.builder().build());
        }
        if (CollectionUtils.isEmpty(resume.getTags())) {
            resume.setTags(Arrays.asList("Resume" /*, resume.getType()*/));
        }
        return super.beforeCreate(resume);
    }
}

