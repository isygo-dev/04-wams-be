package eu.isygoit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.async.kafka.KafkaRegisterAccountProducer;
import eu.isygoit.com.camel.repository.ICamelRepository;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.service.FileImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.AppParameterConstants;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.*;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.dto.request.NewAccountDto;
import eu.isygoit.enums.IEnumAccountOrigin;
import eu.isygoit.enums.IEnumEmailTemplate;
import eu.isygoit.enums.IEnumResumeStatType;
import eu.isygoit.exception.StatisticTypeNotSupportedException;
import eu.isygoit.model.*;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.ims.ImsAppParameterService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.remote.quiz.QuizCandidateQuizService;
import eu.isygoit.repository.AssoAccountResumeRepository;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.ResumeRepository;
import eu.isygoit.service.IMsgService;
import eu.isygoit.service.IResumeService;
import eu.isygoit.types.EmailSubjects;
import eu.isygoit.types.MsgTemplateVariables;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = ResumeRepository.class)
public class ResumeService extends FileImageService<Long, Resume, ResumeRepository>
        implements IResumeService {

    private final AppProperties appProperties;

    private final AssoAccountResumeRepository assoAccountResumeRepository;
    private final IMsgService msgService;
    private final ICamelRepository camelRepository;
    private final ImsAppParameterService imsAppParameterService;
    private final JobOfferApplicationRepository jobApplicationRepository;
    private final KafkaRegisterAccountProducer kafkaRegisterAccountProducer;
    private final QuizCandidateQuizService quizCandidateQuizService;
    private final ResumeStatService resumeStatService;

    @Autowired
    public ResumeService(AppProperties appProperties,
                         AssoAccountResumeRepository assoAccountResumeRepository,
                         IMsgService msgService,
                         ICamelRepository camelRepository,
                         ImsAppParameterService imsAppParameterService,
                         JobOfferApplicationRepository jobApplicationRepository,
                         KafkaRegisterAccountProducer kafkaRegisterAccountProducer,
                         QuizCandidateQuizService quizCandidateQuizService,
                         ResumeStatService resumeStatService) {
        this.appProperties = appProperties;
        this.assoAccountResumeRepository = assoAccountResumeRepository;
        this.msgService = msgService;
        this.camelRepository = camelRepository;
        this.imsAppParameterService = imsAppParameterService;
        this.jobApplicationRepository = jobApplicationRepository;
        this.kafkaRegisterAccountProducer = kafkaRegisterAccountProducer;
        this.quizCandidateQuizService = quizCandidateQuizService;
        this.resumeStatService = resumeStatService;
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
    public List<ResumeShareInfo> shareWithAccounts(Long id, String resumeOwner, List<AccountModelDto> accounts) throws JsonProcessingException {
        Optional<Resume> optional = findById(id);
        optional.ifPresentOrElse(resume -> {
                    List<ResumeShareInfo> shareInfos = accounts.stream().map(acc -> {
                        Optional<ResumeShareInfo> optionalResumeShareInfo = resume.getResumeShareInfos().stream()
                                .filter(resumeShareInfo -> resumeShareInfo.getSharedWith().equals(acc.getCode()))
                                .findFirst();

                        try {
                            shareResumeNotification(resume, resumeOwner, acc);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }

                        return optionalResumeShareInfo
                                .orElse(ResumeShareInfo.builder()
                                        .sharedWith(acc.getCode())
                                        .build());

                    }).collect(Collectors.toUnmodifiableList());

                    resume.getResumeShareInfos().clear();
                    resume.getResumeShareInfos().addAll(shareInfos);
                }, () ->
                        // Handle the case where the Resume with the given ID doesn't exist
                        new NotFoundException("Resume not found with ID: " + id)
        );

        return repository().save(optional.get()).getResumeShareInfos();
    }

    private void shareResumeNotification(Resume resume, String resumeOwner, AccountModelDto account) throws JsonProcessingException {
        final String defaultUrl = "https://localhost:4004/apps/resumes/view/";
        String resumeUrl = Optional.ofNullable(imsAppParameterService.getValueByDomainAndName(RequestContextDto.builder().build(),
                        resume.getDomain(), AppParameterConstants.RESUME_VIEW_URL, true, defaultUrl))
                .filter(result -> result.hasBody() && StringUtils.hasText(result.getBody()))
                .map(result -> result.getBody() + resume.getId())
                .orElse(defaultUrl + resume.getId());  // Default URL fallback


        MailMessageDto mailMessageDto = MailMessageDto.builder()
                .domain(resume.getDomain())
                .subject(EmailSubjects.SHARED_RESUME_EMAIL_SUBJECT)
                .toAddr(account.getEmail())
                .templateName(IEnumEmailTemplate.Types.RESUME_SHARED_TEMPLATE)
                .sent(true)
                .build();

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
    public Optional<Resume> findByCode(String code) {
        Optional<Resume> optionalResume = repository().findByCodeIgnoreCase(code);
        return optionalResume.map(resume -> {
            assoAccountResumeRepository.findByResume_Code(code)
                    .ifPresent(accountResume -> completeSkills(resume, accountResume.getAccountCode()));
            return resume;
        });
    }

    @Override
    public ResumeGlobalStatDto getGlobalStatistics(IEnumResumeStatType.Types statType, RequestContextDto requestContext) {
        // Map each statType to a Supplier that returns the corresponding DTO with only that stat calculated
        Map<IEnumResumeStatType.Types, Supplier<ResumeGlobalStatDto>> statSuppliers = Map.of(
                IEnumResumeStatType.Types.TOTAL_COUNT, () ->
                        ResumeGlobalStatDto.builder()
                                .totalCount(resumeStatService.stat_GetResumesCount(requestContext))
                                .build(),

                IEnumResumeStatType.Types.UPLOADED_BY_ME_COUNT, () ->
                        ResumeGlobalStatDto.builder()
                                .uploadedByMeCount(resumeStatService.stat_GetUploadedByMeResumesCount(requestContext))
                                .build(),

                IEnumResumeStatType.Types.CONFIRMED_COUNT, () ->
                        ResumeGlobalStatDto.builder()
                                .confirmedCount(resumeStatService.stat_GetConfirmedResumesCount(requestContext))
                                .build(),

                IEnumResumeStatType.Types.COMPLETED_COUNT, () ->
                        ResumeGlobalStatDto.builder()
                                .completedCount(resumeStatService.stat_GetCompletedResumesCount(requestContext))
                                .build(),

                IEnumResumeStatType.Types.INTERVIEWED_COUNT, () ->
                        ResumeGlobalStatDto.builder()
                                .interviewedCount(resumeStatService.stat_GetInterviewdResumesCount(requestContext))
                                .build()
        );

        // Return the stat DTO for the requested type or throw if unsupported
        return Optional.ofNullable(statSuppliers.get(statType))
                .map(Supplier::get)
                .orElseThrow(() -> new StatisticTypeNotSupportedException(statType.name()));
    }

    @Override
    public ResumeStatDto getObjectStatistics(String code, RequestContextDto requestContext) {
        return ResumeStatDto.builder()
                .completion(75L)
                .realizedTestsCount(resumeStatService.stat_GetInterviewedResumesCount(code))
                .applicationsCount(resumeStatService.stat_GetJobApplicationResumesCount(code, requestContext))
                .ongoingApplicationsCount(resumeStatService.stat_GetOngoingJobApplicationResumesCount(code, requestContext))
                .build();
    }

    @Override
    public String getAccountCode(String resumeCode) {
        Optional<AssoAccountResume> assoAccountResume = assoAccountResumeRepository.findByResume_Code(resumeCode);
        return assoAccountResume.isPresent() ? assoAccountResume.get().getAccountCode() : null;
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
        Optional<Resume> optional = this.findById(resume.getId());
        if (optional.isPresent() && CollectionUtils.isEmpty(optional.get().getTags())) {
            optional.get().setTags(Arrays.asList("Resume" /*, resume.getType()*/));
        }
        return super.beforeUpdate(optional.get());
    }

    @Override
    public Resume beforeCreate(Resume resume) {
        if (Objects.isNull(resume.getDetails())) {
            resume.setDetails(ResumeDetails.builder().build());
        }
        if (CollectionUtils.isEmpty(resume.getTags())) {
            resume.setTags(Arrays.asList("Resume" /*, resume.getType()*/));
        }
        return super.beforeCreate(resume);
    }
}

