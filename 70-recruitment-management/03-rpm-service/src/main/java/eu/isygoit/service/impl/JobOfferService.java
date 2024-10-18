package eu.isygoit.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.AppParameterConstants;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.LinkedFileRequestDto;
import eu.isygoit.dto.common.LinkedFileResponseDto;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.DomainDto;
import eu.isygoit.dto.data.JobOfferGlobalStatDto;
import eu.isygoit.dto.data.JobOfferStatDto;
import eu.isygoit.dto.data.MailMessageDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.encrypt.helper.CRC16;
import eu.isygoit.encrypt.helper.CRC32;
import eu.isygoit.enums.IEnumMsgTemplateName;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.exception.ResumeNotFoundException;
import eu.isygoit.exception.StatisticTypeNotSupportedException;
import eu.isygoit.model.*;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.ims.ImsAppParameterService;
import eu.isygoit.remote.ims.ImsDomainService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.JobOfferLinkedFileRepository;
import eu.isygoit.repository.JobOfferRepository;
import eu.isygoit.repository.JobOfferTemplateRepository;
import eu.isygoit.service.IJobOfferService;
import eu.isygoit.service.IMsgService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The type Job offer service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = JobOfferRepository.class)
public class JobOfferService extends CodifiableService<Long, JobOffer, JobOfferRepository>
        implements IJobOfferService {

    private final AppProperties appProperties;
    @Autowired
    private JobOfferRepository jobOfferRepository;
    @Autowired
    private DmsLinkedFileService dmsLinkedFileService;
    @Autowired
    private JobOfferLinkedFileRepository jobLinkedFileRepository;
    @Autowired
    private IMsgService msgService;
    @Autowired
    private ImsAppParameterService ImsAppParameterService;
    @Autowired
    private ImsDomainService imsDomainService;
    @Autowired
    private JobOfferTemplateRepository jobTemplateRepository;
    @Autowired
    private JobOfferApplicationRepository jobApplicationRepository;

    /**
     * Instantiates a new Job offer service.
     *
     * @param appProperties the app properties
     */
    public JobOfferService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

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

    @Override
    public List<JobOffer> findJobOffersNotAssignedToResume(String resumeCode) {
        return jobOfferRepository.findJobOffersNotAssignedToResume(resumeCode);
    }

    @Override
    public List<JobOfferLinkedFile> uploadAdditionalFile(Long parentId, MultipartFile[] files) throws IOException {
        JobOffer jobOffer = findById(parentId);
        if (jobOffer != null) {
            for (MultipartFile file : files) {
                try {
                    ResponseEntity<LinkedFileResponseDto> result = dmsLinkedFileService.upload(//RequestContextDto.builder().build(),
                            LinkedFileRequestDto.builder()
                                    .domain(jobOffer.getDomain())
                                    .path(File.separator + "job" + File.separator + "additional")
                                    .tags(Arrays.asList("Job"))
                                    .categoryNames(Arrays.asList("Job"))
                                    .file(file)
                                    .build());
                    if (result.getStatusCode().is2xxSuccessful() && result.hasBody()) {
                        log.info("File uploaded successfully {}", file.getOriginalFilename());
                        JobOfferLinkedFile jobLinkedFile = JobOfferLinkedFile.builder()
                                .code(result.getBody().getCode())
                                .originalFileName(file.getOriginalFilename())
                                .extension(FilenameUtils.getExtension(file.getOriginalFilename()))
                                .crc16(CRC16.calculate(file.getBytes()))
                                .crc32(CRC32.calculate(file.getBytes()))
                                .size(file.getSize())
                                .path("/job/additional")
                                .mimetype(file.getContentType())
                                .version(1L)
                                .build();
                        if (CollectionUtils.isEmpty(jobOffer.getAdditionalFiles())) {
                            jobOffer.setAdditionalFiles(new ArrayList<>());
                        }
                        jobOffer.getAdditionalFiles().add(jobLinkedFile);
                        jobOffer = this.update(jobOffer);
                    }
                } catch (Exception e) {
                    log.error("Remote feign call failed : ", e);
                    //throw new RemoteCallFailedException(e);
                }

            }
            return jobOffer.getAdditionalFiles();
        } else {
            throw new ResumeNotFoundException("with id: " + parentId);
        }
    }

    @Override
    public boolean deleteAdditionalFile(Long parentId, Long fileId) throws IOException {
        JobOffer jobOffer = findById(parentId);
        if (jobOffer != null) {
            JobOfferLinkedFile jobLinkedFile = jobOffer.getAdditionalFiles().stream()
                    .filter((JobOfferLinkedFile item) -> item.getId().equals(fileId)).findAny()
                    .orElse(null);
            if (jobLinkedFile != null) {
                jobOffer.getAdditionalFiles().removeIf(elm -> elm.getId().equals(fileId));
                dmsLinkedFileService.deleteFile(RequestContextDto.builder().build(), jobOffer.getDomain(), jobLinkedFile.getCode());
                this.update(jobOffer);
                jobLinkedFileRepository.deleteById(jobLinkedFile.getId());
                return true;
            } else {
                throw new FileNotFoundException("with id " + fileId);
            }
        } else {
            throw new ResumeNotFoundException("with id: " + parentId);
        }
    }

    @Override
    public List<JobOfferShareInfo> shareJob(Long id, String jobOwner, List<AccountModelDto> account) throws JsonProcessingException {
        JobOffer job = findById(id);
        List<JobOfferShareInfo> shareInfos = new ArrayList<>();
        if (job != null) {
            for (AccountModelDto acc : account) {
                boolean exists = false;
                JobOfferShareInfo existingShareInfo = null;

                for (JobOfferShareInfo shareInfo : job.getJobShareInfos()) {
                    if (shareInfo.getSharedWith().equals(acc.getCode())) {
                        exists = true;
                        existingShareInfo = shareInfo;
                        break;
                    }
                }

                if (!exists) {
                    JobOfferShareInfo shareInfo = new JobOfferShareInfo();
                    shareInfo.setSharedWith(acc.getCode());
                    shareInfos.add(shareInfo);
                    shareJobNotification(job, jobOwner, acc);
                } else {
                    shareInfos.add(existingShareInfo);
                }
            }
            job.getJobShareInfos().clear();
            job.getJobShareInfos().addAll(shareInfos);
            return jobOfferRepository.save(job).getJobShareInfos();
        } else {
            throw new NotFoundException("Resume not found with ID: " + id);
        }
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
        //Send email/notification to the board watchers
        String jobUrl = "https://localhost:4002/apps/job/";
        try {
            ResponseEntity<String> result = ImsAppParameterService.getValueByDomainAndName(RequestContextDto.builder().build(),
                    jobOffer.getDomain(), AppParameterConstants.JOB_VIEW_URL, true, "https://localhost:4002/apps/job/");
            if (result.hasBody() && StringUtils.hasText(result.getBody())) {
                jobUrl = result.getBody() + jobOffer.getId();
            }
        } catch (Exception e) {
            log.error("Remote feign call failed : ", e);
            //throw new RemoteCallFailedException(e);
        }


        MailMessageDto mailMessageDto = MailMessageDto.builder()
                .domain(jobOffer.getDomain())
                .subject(EmailSubjects.SHARED_JOB_EMAIL_SUBJECT)
                .toAddr(account.getEmail())
                .templateName(IEnumMsgTemplateName.Types.JOB_OFFER_SHARED_TEMPLATE)
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
                MsgTemplateVariables.V_DOMAIN_NAME, jobOffer.getDomain(),
                //Specific vars
                MsgTemplateVariables.V_JOB_TITLE, jobOffer.getTitle(),
                MsgTemplateVariables.V_SHARED_BY, jobOffer.getOwner(),
                MsgTemplateVariables.V_JOB_VIEW_URL, jobUrl)));

        msgService.sendMessage(jobOffer.getDomain(), mailMessageDto, appProperties.isSendAsyncEmail());
    }

    @Override
    public JobOffer afterCreate(JobOffer jobOffer) {
        if (jobOffer.getDetails() == null) {
            jobOffer.setDetails(new JobOfferDetails());
        }
        return super.afterCreate(jobOffer);
    }

    @Override
    public void afterDelete(Long id) {
        jobTemplateRepository.deleteJobTemplateByJobOffer_Id(id);
        super.afterDelete(id);
    }
}
