package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.CrudService;
import eu.isygoit.model.JobOfferTemplate;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.JobOfferTemplateRepository;
import eu.isygoit.service.IJobOfferTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = JobOfferTemplateRepository.class)
public class JobOfferTemplateService extends CrudService<Long, JobOfferTemplate, JobOfferTemplateRepository> implements IJobOfferTemplateService {

}
