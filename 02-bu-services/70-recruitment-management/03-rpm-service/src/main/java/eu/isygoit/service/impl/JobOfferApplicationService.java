package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.exception.ObjectNotFoundException;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.repository.code.NextCodeRepository;
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
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = JobOfferApplicationRepository.class)
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
        jobApplication.setTenant(jobApplication.getResume().getTenant());

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
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(JobOfferApplication.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("JAP")
                .valueLength(6L)
                .codeValue(1L)
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
