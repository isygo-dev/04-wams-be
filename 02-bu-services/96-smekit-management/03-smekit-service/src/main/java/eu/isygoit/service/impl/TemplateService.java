package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.com.rest.service.impl.FileService;
import eu.isygoit.com.rest.service.impl.FileServiceSubMethods;
import eu.isygoit.com.rest.service.impl.MultiFileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Template;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = TemplateRepository.class)
public class TemplateService  extends FileService<Long, Template, TemplateRepository> implements ITemplateService  {
    private final AppProperties appProperties;


    public TemplateService(AppProperties appProperties) {
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
                .entity(Template.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("TMP")
                .valueLength(6L)
                .value(1L)
                .build();
    }
}
