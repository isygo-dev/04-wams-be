package eu.isygoit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.AppParameterConstants;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferGlobalStatDto;
import eu.isygoit.dto.data.JobOfferStatDto;
import eu.isygoit.dto.data.MailMessageDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.enums.IEnumEmailTemplate;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.exception.JobOfferNotFoundException;
import eu.isygoit.exception.ShareJobNotificationException;
import eu.isygoit.exception.StatisticTypeNotSupportedException;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.JobOffer;
import eu.isygoit.model.JobOfferDetails;
import eu.isygoit.model.JobOfferShareInfo;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.ims.ImsAppParameterService;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = JobOfferRepository.class)
public class JobOfferService extends CodeAssignableService<Long, JobOffer, JobOfferRepository>
        implements IJobOfferService {

    private final AppProperties appProperties;
    private final IMsgService msgService;
    private final ImsAppParameterService imsAppParameterService;
    private final JobOfferTemplateRepository jobTemplateRepository;
    private final JobOfferApplicationRepository jobApplicationRepository;
    private final JobOfferStatService jobOfferStatService;

    @Autowired
    public JobOfferService(AppProperties appProperties,
                           IMsgService msgService,
                           ImsAppParameterService imsAppParameterService,
                           JobOfferTemplateRepository jobTemplateRepository,
                           JobOfferApplicationRepository jobApplicationRepository,
                           JobOfferStatService jobOfferStatService) {
        this.appProperties = appProperties;
        this.msgService = msgService;
        this.imsAppParameterService = imsAppParameterService;
        this.jobTemplateRepository = jobTemplateRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.jobOfferStatService = jobOfferStatService;
    }

    /**
     * Initializes code generator for JobOffer entity with default domain and prefix.
     */
    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(JobOffer.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("JOB")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }

    /**
     * Finds all job offers that are not assigned to the specified resume code.
     */
    @Override
    public List<JobOffer> findJobOffersNotAssignedToResume(String resumeCode) {
        return repository().findJobOffersNotAssignedToResume(resumeCode);
    }

    /**
     * Shares a job offer with a list of accounts and sends notification emails.
     */
    @Override
    public List<JobOfferShareInfo> shareJob(Long id, String jobOwner, List<AccountModelDto> accounts) throws JsonProcessingException {
        return findById(id)
                .map(jobOffer -> {
                    // Create or reuse share info for each account
                    List<JobOfferShareInfo> shareInfos = accounts.stream()
                            .map(account -> jobOffer.getJobShareInfos().stream()
                                    .filter(info -> info.getSharedWith().equals(account.getCode()))
                                    .findFirst()
                                    .orElseGet(() -> JobOfferShareInfo.builder()
                                            .sharedWith(account.getCode())
                                            .build()))
                            .collect(Collectors.toList());

                    // Update share infos on job offer
                    jobOffer.getJobShareInfos().clear();
                    jobOffer.getJobShareInfos().addAll(shareInfos);

                    // Send notifications outside stream for clarity and better error handling
                    accounts.forEach(account -> {
                        try {
                            shareJobNotification(jobOffer, jobOwner, account);
                        } catch (JsonProcessingException e) {
                            throw new ShareJobNotificationException("Failed to send notification for account " + account.getCode());
                        }
                    });

                    // Save and return updated share infos
                    return repository().save(jobOffer).getJobShareInfos();
                })
                .orElseThrow(() -> new JobOfferNotFoundException("Job offer not found with ID: " + id));
    }

    /**
     * Returns the global statistics based on the requested stat type.
     * Uses lazy evaluation to calculate only the requested statistic.
     */
    @Override
    public JobOfferGlobalStatDto getGlobalStatistics(IEnumSharedStatType.Types statType, RequestContextDto requestContext) {
        // Map each statType to a Supplier that returns the corresponding DTO with only that stat calculated
        Map<IEnumSharedStatType.Types, Supplier<JobOfferGlobalStatDto>> statSuppliers = Map.of(
                IEnumSharedStatType.Types.TOTAL_COUNT, () ->
                        JobOfferGlobalStatDto.builder()
                                .totalCount(jobOfferStatService.stat_GetJobOffersCount(requestContext))
                                .build(),

                IEnumSharedStatType.Types.ACTIVE_COUNT, () ->
                        JobOfferGlobalStatDto.builder()
                                .activeCount(jobOfferStatService.stat_GetActiveJobOffersCount(requestContext))
                                .build(),

                IEnumSharedStatType.Types.CONFIRMED_COUNT, () ->
                        JobOfferGlobalStatDto.builder()
                                .confirmedCount(jobOfferStatService.stat_GetConfirmedJobOffersCount(requestContext))
                                .build(),

                IEnumSharedStatType.Types.EXPIRED_COUNT, () ->
                        JobOfferGlobalStatDto.builder()
                                .expiredCount(jobOfferStatService.stat_GetExpiredJobOffersCount(requestContext))
                                .build()
        );

        // Return the stat DTO for the requested type or throw if unsupported
        return Optional.ofNullable(statSuppliers.get(statType))
                .map(Supplier::get)  // Executes exactly one method
                .orElseThrow(() -> new StatisticTypeNotSupportedException(statType.name()));
    }

    /**
     * Returns detailed statistics for a specific job offer identified by code.
     */
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

    /**
     * Gets the number of applications for the given job code and domain.
     */
    private Long getNumberOfApplicationsByJob(String code, RequestContextDto requestContext) {
        return jobApplicationRepository.countJobsByNumberOfApplications(code, requestContext.getSenderDomain());
    }

    /**
     * Gets the number of interviewed profiles for the given job code and domain.
     */
    private Long getInterviewedProfilesCount(String code, RequestContextDto requestContext) {
        // Note: Uses same method as applications count - verify if this is intentional
        return jobApplicationRepository.countJobsByNumberOfApplications(code, requestContext.getSenderDomain());
    }

    /**
     * Sends a share notification email for a job offer to the specified account.
     */
    private void shareJobNotification(JobOffer jobOffer, String jobOwner, AccountModelDto account) throws JsonProcessingException {
        final String defaultUrl = "https://localhost:4002/apps/job/";

        // Fetch job view URL parameter, fallback to default if unavailable
        String jobUrl = Optional.ofNullable(imsAppParameterService.getValueByDomainAndName(RequestContextDto.builder().build(),
                        jobOffer.getDomain(), AppParameterConstants.JOB_VIEW_URL, true, defaultUrl))
                .filter(result -> result.hasBody() && StringUtils.hasText(result.getBody()))
                .map(result -> result.getBody() + jobOffer.getId())
                .orElse(defaultUrl + jobOffer.getId());

        // Build mail message DTO
        MailMessageDto mailMessageDto = MailMessageDto.builder()
                .domain(jobOffer.getDomain())
                .subject(EmailSubjects.SHARED_JOB_EMAIL_SUBJECT)
                .toAddr(account.getEmail())
                .templateName(IEnumEmailTemplate.Types.JOB_OFFER_SHARED_TEMPLATE)
                .sent(true)
                .build();

        // Set variables for the email template
        mailMessageDto.setVariables(MailMessageDto.getVariablesAsString(Map.of(
                MsgTemplateVariables.V_USER_NAME, account.getCode(),
                MsgTemplateVariables.V_FULLNAME, account.getFullName(),
                MsgTemplateVariables.V_DOMAIN_NAME, jobOffer.getDomain(),
                MsgTemplateVariables.V_JOB_TITLE, jobOffer.getTitle(),
                MsgTemplateVariables.V_SHARED_BY, jobOffer.getOwner(),
                MsgTemplateVariables.V_JOB_VIEW_URL, jobUrl)));

        // Send the email (async if configured)
        msgService.sendMessage(jobOffer.getDomain(), mailMessageDto, appProperties.isSendAsyncEmail());
    }

    /**
     * Ensures that JobOfferDetails is initialized after creation.
     */
    @Override
    public JobOffer afterCreate(JobOffer jobOffer) {
        if (Objects.isNull(jobOffer.getDetails())) {
            jobOffer.setDetails(JobOfferDetails.builder().build());
        }
        return super.afterCreate(jobOffer);
    }

    /**
     * Deletes associated job templates after deleting a job offer.
     */
    @Override
    public void afterDelete(Long id) {
        jobTemplateRepository.deleteJobTemplateByJobOffer_Id(id);
        super.afterDelete(id);
    }
}