package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.exception.ObjectNotFoundException;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.JobOffer;
import eu.isygoit.model.JobOfferApplication;
import eu.isygoit.model.Resume;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.JobOfferRepository;
import eu.isygoit.repository.ResumeRepository;
import eu.isygoit.service.IJobOfferApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = JobOfferApplicationRepository.class)
public class JobOfferApplicationService
        extends CodeAssignableService<Long, JobOfferApplication, JobOfferApplicationRepository>
        implements IJobOfferApplicationService {

    private final ResumeRepository resumeRepository;
    private final JobOfferRepository jobOfferRepository;

    @Autowired
    public JobOfferApplicationService(ResumeRepository resumeRepository, JobOfferRepository jobOfferRepository) {
        this.resumeRepository = resumeRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    @Override
    public JobOfferApplication beforeCreate(JobOfferApplication jobApplication) {
        jobApplication.setResume(findResumeByCode(jobApplication.getResume().getCode()));
        jobApplication.setDomain(jobApplication.getResume().getDomain());

        jobApplication.setJobOffer(findJobOfferByCode(jobApplication.getJobOffer().getCode()));

        return super.beforeCreate(jobApplication);
    }

    @Override
    public JobOfferApplication beforeUpdate(JobOfferApplication jobApplication) {
        jobApplication.setResume(findResumeByCode(jobApplication.getResume().getCode()));
        jobApplication.setJobOffer(findJobOfferByCode(jobApplication.getJobOffer().getCode()));

        return super.beforeUpdate(jobApplication);
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(JobOfferApplication.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("JAP")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }

    private Resume findResumeByCode(String code) {
        return resumeRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new ObjectNotFoundException("Resume with code: " + code));
    }

    private JobOffer findJobOfferByCode(String code) {
        return jobOfferRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new ObjectNotFoundException("JobOffer with code: " + code));
    }
}
