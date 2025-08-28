package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.repository.code.NextCodeRepository;
import eu.isygoit.model.Template;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.ITemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = TemplateRepository.class)
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
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(Template.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("TMP")
                .valueLength(6L)
                .codeValue(1L)
                .build();
    }

    @Transactional
    public Template updateTemplate(Template template) {
        return templateRepository.save(template);
    }

    public List<Template> getTemplatesByCategory(Long categoryId) {
        return templateRepository.findByCategoryId(categoryId);
    }

    public List<Template> findAllByFavoritesContaining(String userName) {
        return templateRepository.findAllByFavoritesContaining(userName);
    }

    public Template findAllByIdAndFavoritesContaining(Long id, String userName) {
        return templateRepository.findAllByIdAndFavoritesContaining(id, userName);
    }

    public Long countTemplatesWithFavoriteUser(String userName) {
        return templateRepository.countTemplatesWithFavoriteUser(userName);
    }
}