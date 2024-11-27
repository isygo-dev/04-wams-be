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

/**
 * The type Job offer application service.
 */
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = JobOfferApplicationRepository.class)
public class JobOfferApplicationService extends CodifiableService<Long, JobOfferApplication, JobOfferApplicationRepository> implements IJobOfferApplicationService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Override
    public JobOfferApplication beforeCreate(JobOfferApplication jobApplication) {
        Optional<Resume> resume = resumeRepository.findByCodeIgnoreCase(jobApplication.getResume().getCode());
        if (resume.isPresent()) {
            jobApplication.setResume(resume.get());
            jobApplication.setDomain(resume.get().getDomain());
        }

        Optional<JobOffer> jobOffer = jobOfferRepository.findByCodeIgnoreCase(jobApplication.getJobOffer().getCode());
        if (jobOffer.isPresent()) {
            jobApplication.setJobOffer(jobOffer.get());
        }
        //init job application status
        return super.beforeCreate(jobApplication);
    }

    @Override
    public JobOfferApplication beforeUpdate(JobOfferApplication jobApplication) {
        if (jobApplication.getResume().getCode() != null) {
            Optional<Resume> optional = resumeRepository.findByCodeIgnoreCase(jobApplication.getResume().getCode());
            if (optional.isPresent()) {
                jobApplication.setResume(optional.get());
            }
        }
        if (jobApplication.getJobOffer().getCode() != null) {
            Optional<JobOffer> optional = jobOfferRepository.findByCodeIgnoreCase(jobApplication.getJobOffer().getCode());
            if (optional.isPresent()) {
                jobApplication.setJobOffer(optional.get());
            }
        }
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
}
