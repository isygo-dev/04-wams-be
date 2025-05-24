package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Template;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = TemplateRepository.class)
public class TemplateService extends FileService<Long, Template, TemplateRepository> implements ITemplateService {

    private final AppProperties appProperties;
    private final TemplateRepository templateRepository;

    public TemplateService(AppProperties appProperties,
                           TemplateRepository templateRepository) {
        this.appProperties = appProperties;
        this.templateRepository = templateRepository;

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

    @Transactional
    public Template updateTemplate(Template template) {
        template.setEditionDate(LocalDateTime.now());
        return templateRepository.save(template);
    }

    public List<Template> getTemplatesByCategory(Long categoryId) {
        return templateRepository.findByCategoryId(categoryId);
    }


}