package eu.isygoit.service.impl;

import eu.isygoit.constants.TenantConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.model.AssoAccountResume;
import eu.isygoit.remote.ims.ImAccountService;
import eu.isygoit.remote.quiz.QuizCandidateQuizService;
import eu.isygoit.repository.AssoAccountResumeRepository;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.ResumeRepository;
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

    public Long stat_GetCompletedResumesCount(ContextRequestDto requestContext) {
        return 58L;
    }

    public Long stat_GetResumesCount(ContextRequestDto requestContext) {
        if (TenantConstants.SUPER_TENANT_NAME.equals(requestContext.getSenderTenant())) {
            return resumeRepository.count();
        } else {
            return resumeRepository.countByTenantIgnoreCase(requestContext.getSenderTenant());
        }
    }

    public Long stat_GetUploadedByMeResumesCount(ContextRequestDto requestContext) {
        return resumeRepository.countByCreatedBy(requestContext.getCreatedByString());
    }

    public Long stat_GetConfirmedResumesCount(ContextRequestDto requestContext) {
        ResponseEntity<Long> responseEntity = imAccountService.getConfirmedResumeAccountsCount(requestContext);
        return responseEntity.getBody();
    }

    public Long stat_GetInterviewdResumesCount(ContextRequestDto requestContext) {
        return jobApplicationRepository.countOngoingGlobalJobApplication(requestContext.getSenderTenant());
    }

    public Long stat_GetInterviewedResumesCount(String code) {
        Optional<AssoAccountResume> assoAccountResume = assoAccountResumeRepository.findByResume_Code(code);
        if (assoAccountResume.isPresent()) {
            ResponseEntity<Long> responseEntity = quizCandidateQuizService.getCountRealizedTestByAccount(assoAccountResume.get().getAccountCode());
            return responseEntity.getBody();
        }
        return 0L;
    }

    public Long stat_GetJobApplicationResumesCount(String code, ContextRequestDto requestContext) {
        return jobApplicationRepository.countByResumeCodeAndTenant(code, requestContext.getSenderTenant());
    }

    public Long stat_GetOngoingJobApplicationResumesCount(String code, ContextRequestDto requestContext) {
        return jobApplicationRepository.countOnGoingJobApplicationByResume(requestContext.getSenderTenant(), code);
    }
}