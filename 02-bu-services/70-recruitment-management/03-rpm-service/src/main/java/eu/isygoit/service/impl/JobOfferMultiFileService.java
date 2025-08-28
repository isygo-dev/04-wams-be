package eu.isygoit.service.impl;

import eu.isygoit.annotation.*;
import eu.isygoit.com.rest.service.MultiFileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.repository.code.NextCodeRepository;
import eu.isygoit.model.JobOffer;
import eu.isygoit.model.JobOfferLinkedFile;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.JobOfferLinkedFileRepository;
import eu.isygoit.repository.JobOfferRepository;
import eu.isygoit.service.IJobOfferMultiFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@InjectDmsLinkedFileService(DmsLinkedFileService.class)
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = JobOfferRepository.class)
@InjectLinkedFileRepository(value = JobOfferLinkedFileRepository.class)
public class JobOfferMultiFileService extends MultiFileService<Long, JobOffer, JobOfferLinkedFile, JobOfferRepository, JobOfferLinkedFileRepository>
        implements IJobOfferMultiFileService {

    private final AppProperties appProperties;

    @Autowired
    public JobOfferMultiFileService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(JobOffer.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("JMF")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }
}
