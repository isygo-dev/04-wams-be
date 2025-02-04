package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.JobOffer;
import eu.isygoit.model.JobOfferApplication;
import eu.isygoit.model.Resume;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.JobOfferApplicationRepository;
import eu.isygoit.repository.JobOfferRepository;
import eu.isygoit.repository.ResumeRepository;
import eu.isygoit.service.IJobOfferApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = JobOfferApplicationRepository.class)
public class JobOfferApplicationService extends CodifiableService<Long, JobOfferApplication, JobOfferApplicationRepository> implements IJobOfferApplicationService {

    private final ResumeRepository resumeRepository;
    private final JobOfferRepository jobOfferRepository;

    @Autowired
    public JobOfferApplicationService(ResumeRepository resumeRepository, JobOfferRepository jobOfferRepository) {
        this.resumeRepository = resumeRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    @Override
    public JobOfferApplication beforeCreate(JobOfferApplication jobApplication) {
        resumeRepository.findByCodeIgnoreCase(jobApplication.getResume().getCode())
                .ifPresent(resume -> {
                    jobApplication.setResume(resume);
                    jobApplication.setDomain(resume.getDomain());
                });

        jobOfferRepository.findByCodeIgnoreCase(jobApplication.getJobOffer().getCode())
                .ifPresent(jobOffer -> jobApplication.setJobOffer(jobOffer));

        return super.beforeCreate(jobApplication);
    }

    @Override
    public JobOfferApplication beforeUpdate(JobOfferApplication jobApplication) {
        resumeRepository.findByCodeIgnoreCase(jobApplication.getResume().getCode())
                .ifPresent(resume -> {
                    jobApplication.setResume(resume);
                });

        jobOfferRepository.findByCodeIgnoreCase(jobApplication.getJobOffer().getCode())
                .ifPresent(jobOffer -> jobApplication.setJobOffer(jobOffer));

        return super.beforeUpdate(jobApplication);
    }

    @Override
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(JobOfferApplication.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("JAP")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build());
    }
}
