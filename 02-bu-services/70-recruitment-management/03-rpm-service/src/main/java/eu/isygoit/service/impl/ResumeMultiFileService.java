package eu.isygoit.service.impl;

import eu.isygoit.annotation.*;
import eu.isygoit.com.rest.service.MultiFileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Resume;
import eu.isygoit.model.ResumeLinkedFile;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.ResumeLinkedFileRepository;
import eu.isygoit.repository.ResumeRepository;
import eu.isygoit.service.IResumeMultiFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = ResumeRepository.class)
@ServLinkFileRepo(value = ResumeLinkedFileRepository.class)
public class ResumeMultiFileService extends MultiFileService<Long, Resume, ResumeLinkedFile, ResumeRepository, ResumeLinkedFileRepository>
        implements IResumeMultiFileService {

    private final AppProperties appProperties;

    @Autowired
    public ResumeMultiFileService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Resume.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("RMF")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }
}
