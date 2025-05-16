package eu.isygoit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.AppParameterConstants;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferGlobalStatDto;
import eu.isygoit.dto.data.JobOfferStatDto;
import eu.isygoit.dto.data.MailMessageDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.enums.IEnumMsgTemplateName;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.exception.JobOfferNotFoundException;
import eu.isygoit.exception.ShareJobNotificationException;
import eu.isygoit.exception.StatisticTypeNotSupportedException;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.JobOffer;
import eu.isygoit.model.JobOfferDetails;
import eu.isygoit.model.JobOfferShareInfo;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.ims.ImsAppParameterService;
import eu.isygoit.remote.ims.ImsDomainService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.JobOfferRepository;
import eu.isygoit.repository.JobOfferTemplateRepository;
import eu.isygoit.service.IJobOfferService;
import eu.isygoit.service.IMsgService;
import eu.isygoit.types.EmailSubjects;
import eu.isygoit.types.MsgTemplateVariables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = JobOfferRepository.class)
public class JobOfferService extends CodifiableService<Long, JobOffer, JobOfferRepository>
        implements IJobOfferService {

    private final AppProperties appProperties;
    private final JobOfferRepository jobOfferRepository;
    private final IMsgService msgService;
    private final ImsAppParameterService imsAppParameterService;
    private final ImsDomainService imsDomainService;
    private final JobOfferTemplateRepository jobTemplateRepository;
    private final JobOfferApplicationRepository jobApplicationRepository;

    @Autowired
    public JobOfferService(AppProperties appProperties, JobOfferRepository jobOfferRepository, IMsgService msgService, ImsAppParameterService ImsAppParameterService, ImsDomainService imsDomainService, JobOfferTemplateRepository jobTemplateRepository, JobOfferApplicationRepository jobApplicationRepository) {
        this.appProperties = appProperties;
        this.jobOfferRepository = jobOfferRepository;
        this.msgService = msgService;
        this.imsAppParameterService = ImsAppParameterService;
        this.imsDomainService = imsDomainService;
        this.jobTemplateRepository = jobTemplateRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(JobOffer.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("JOB")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build());
    }

    @Override
    public List<JobOffer> findJobOffersNotAssignedToResume(String resumeCode) {
        return jobOfferRepository.findJobOffersNotAssignedToResume(resumeCode);
    }

    @Override
    public List<JobOfferShareInfo> shareJob(Long id, String jobOwner, List<AccountModelDto> accounts) throws JsonProcessingException {
        return findById(id)
                .map(jobOffer -> {
                    // Create the share information list using a stream
                    List<JobOfferShareInfo> shareInfos = accounts.stream()
                            .map(accountModelDto -> {
                                // Check if the share info already exists using Optional
                                return jobOffer.getJobShareInfos().stream()
                                        .filter(shareInfo -> shareInfo.getSharedWith().equals(accountModelDto.getCode()))
                                        .findFirst()
                                        .orElseGet(() -> JobOfferShareInfo.builder()
                                                .sharedWith(accountModelDto.getCode())
                                                .build());
                            })
                            .collect(Collectors.toList());

                    // Update the job offer's share info list
                    jobOffer.getJobShareInfos().clear();
                    jobOffer.getJobShareInfos().addAll(shareInfos);

                    // Send notifications (move it outside stream for clarity)
                    accounts.forEach(accountModelDto -> {
                        try {
                            shareJobNotification(jobOffer, jobOwner, accountModelDto);
                        } catch (JsonProcessingException e) {
                            throw new ShareJobNotificationException("Failed to send notification for account " + accountModelDto.getCode());
                        }
                    });

                    // Save and return updated share info list
                    return jobOfferRepository.save(jobOffer).getJobShareInfos();
                })
                .orElseThrow(() -> new JobOfferNotFoundException("with ID: " + id));
    }

    @Override
    public JobOfferGlobalStatDto getGlobalStatistics(IEnumSharedStatType.Types statType, RequestContextDto requestContext) {
        JobOfferGlobalStatDto.JobOfferGlobalStatDtoBuilder builder = JobOfferGlobalStatDto.builder();
        int confirmedCount = 182;

        switch (statType) {
            case TOTAL_COUNT:
                builder.totalCount(stat_GetJobOffersCount(requestContext));
                break;
            case ACTIVE_COUNT:
                builder.activeCount(stat_GetActiveJobOffersCount(requestContext));
                break;
            case CONFIRMED_COUNT:
                builder.confirmedCount(stat_GetConfirmedJobOffersCount(requestContext));
                break;
            case EXPIRED_COUNT:
                builder.expiredCount(stat_GetExpiredJobOffersCount(requestContext));
                break;
            default:
                throw new StatisticTypeNotSupportedException(statType.name());
        }

        return builder.build();
    }

    private Long stat_GetConfirmedJobOffersCount(RequestContextDto requestContext) {
        return 181L;
    }

    private Long stat_GetJobOffersCount(RequestContextDto requestContext) {
        return repository().countByDomainIgnoreCase(requestContext.getSenderDomain());
    }

    private Long stat_GetActiveJobOffersCount(RequestContextDto requestContext) {
        return repository().countByDomainAndDeadLine(requestContext.getSenderDomain());
    }

    private Long stat_GetExpiredJobOffersCount(RequestContextDto requestContext) {
        return repository().countByDomainAndExpiredDeadLine(requestContext.getSenderDomain());
    }

    @Override
    public JobOfferStatDto getObjectStatistics(String code, RequestContextDto requestContext) {
        return JobOfferStatDto.builder()
                .completion(75L)
                .applicationCount(getNumberOfApplicationsByJob(code, requestContext))
                .selectedProfilesCount(26L)
                .interviewedProfilesCount(getInterviewedProfilesCount(code, requestContext))
                .rejectedProfilesCount(8L)
                .build();
    }

    private Long getNumberOfApplicationsByJob(String code, RequestContextDto requestContext) {
        return jobApplicationRepository.countJobsByNumberOfApplications(code, requestContext.getSenderDomain());
    }

    private Long getInterviewedProfilesCount(String code, RequestContextDto requestContext) {
        return jobApplicationRepository.countJobsByNumberOfApplications(code, requestContext.getSenderDomain());
    }

    private void shareJobNotification(JobOffer jobOffer, String jobOwner, AccountModelDto account) throws JsonProcessingException {
        final String defaultUrl = "https://localhost:4002/apps/job/";
        String jobUrl = Optional.ofNullable(imsAppParameterService.getValueByDomainAndName(RequestContextDto.builder().build(),
                        jobOffer.getDomain(), AppParameterConstants.JOB_VIEW_URL, true, defaultUrl))
                .filter(result -> result.hasBody() && StringUtils.hasText(result.getBody()))
                .map(result -> result.getBody() + jobOffer.getId())
                .orElse(defaultUrl + jobOffer.getId());  // Default URL fallback

        // Create mail message DTO
        MailMessageDto mailMessageDto = MailMessageDto.builder()
                .domain(jobOffer.getDomain())
                .subject(EmailSubjects.SHARED_JOB_EMAIL_SUBJECT)
                .toAddr(account.getEmail())
                .templateName(IEnumMsgTemplateName.Types.JOB_OFFER_SHARED_TEMPLATE)
                .sent(true)
                .build();

        // Set variables in the mail message DTO
        mailMessageDto.setVariables(MailMessageDto.getVariablesAsString(Map.of(
                MsgTemplateVariables.V_USER_NAME, account.getCode(),
                MsgTemplateVariables.V_FULLNAME, account.getFullName(),
                MsgTemplateVariables.V_DOMAIN_NAME, jobOffer.getDomain(),
                MsgTemplateVariables.V_JOB_TITLE, jobOffer.getTitle(),
                MsgTemplateVariables.V_SHARED_BY, jobOffer.getOwner(),
                MsgTemplateVariables.V_JOB_VIEW_URL, jobUrl)));

        // Send the message
        msgService.sendMessage(jobOffer.getDomain(), mailMessageDto, appProperties.isSendAsyncEmail());
    }

    @Override
    public JobOffer afterCreate(JobOffer jobOffer) {
        if (Objects.isNull(jobOffer.getDetails())) {
            jobOffer.setDetails(JobOfferDetails.builder().build());
        }
        return super.afterCreate(jobOffer);
    }

    @Override
    public void afterDelete(Long id) {
        jobTemplateRepository.deleteJobTemplateByJobOffer_Id(id);
        super.afterDelete(id);
    }
}
