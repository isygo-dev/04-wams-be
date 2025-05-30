package eu.isygoit.service.impl;

import eu.isygoit.async.kafka.KafkaRegisterAccountProducer;
import eu.isygoit.com.camel.repository.ICamelRepository;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.model.AssoAccountResume;
import eu.isygoit.remote.ims.ImAccountService;
import eu.isygoit.remote.ims.ImsAppParameterService;
import eu.isygoit.remote.quiz.QuizCandidateQuizService;
import eu.isygoit.repository.AssoAccountResumeRepository;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.ResumeRepository;
import eu.isygoit.service.IMsgService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResumeStatService {

    private final JobOfferApplicationRepository jobApplicationRepository;
    private final ImAccountService imAccountService;
    private final ResumeRepository resumeRepository;
    private final AssoAccountResumeRepository assoAccountResumeRepository;
    private final QuizCandidateQuizService quizCandidateQuizService;

    public ResumeStatService(JobOfferApplicationRepository jobApplicationRepository,
                             ImAccountService imAccountService,
                             ResumeRepository resumeRepository, AssoAccountResumeRepository assoAccountResumeRepository, QuizCandidateQuizService quizCandidateQuizService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.imAccountService = imAccountService;
        this.resumeRepository = resumeRepository;
        this.assoAccountResumeRepository = assoAccountResumeRepository;
        this.quizCandidateQuizService = quizCandidateQuizService;
    }

    public Long stat_GetCompletedResumesCount(RequestContextDto requestContext) {
        return 58L;
    }

    public Long stat_GetResumesCount(RequestContextDto requestContext) {
        if (DomainConstants.SUPER_DOMAIN_NAME.equals(requestContext.getSenderDomain())) {
            return resumeRepository.count();
        } else {
            return resumeRepository.countByDomainIgnoreCase(requestContext.getSenderDomain());
        }
    }

    public Long stat_GetUploadedByMeResumesCount(RequestContextDto requestContext) {
        return resumeRepository.countByCreatedBy(requestContext.getCreatedByString());
    }

    public Long stat_GetConfirmedResumesCount(RequestContextDto requestContext) {
        ResponseEntity<Long> responseEntity = imAccountService.getConfirmedResumeAccountsCount(requestContext);
        return responseEntity.getBody();
    }

    public Long stat_GetInterviewdResumesCount(RequestContextDto requestContext) {
        return jobApplicationRepository.countOngoingGlobalJobApplication(requestContext.getSenderDomain());
    }

    public Long stat_GetInterviewedResumesCount(String code) {
        Optional<AssoAccountResume> assoAccountResume = assoAccountResumeRepository.findByResume_Code(code);
        if (assoAccountResume.isPresent()) {
            ResponseEntity<Long> responseEntity = quizCandidateQuizService.getCountRealizedTestByAccount(assoAccountResume.get().getAccountCode());
            return responseEntity.getBody();
        }
        return 0L;
    }

    public Long stat_GetJobApplicationResumesCount(String code, RequestContextDto requestContext) {
        return jobApplicationRepository.countByResumeCodeAndDomain(code, requestContext.getSenderDomain());
    }

    public Long stat_GetOngoingJobApplicationResumesCount(String code, RequestContextDto requestContext) {
        return jobApplicationRepository.countOnGoingJobApplicationByResume(requestContext.getSenderDomain(), code);
    }
}