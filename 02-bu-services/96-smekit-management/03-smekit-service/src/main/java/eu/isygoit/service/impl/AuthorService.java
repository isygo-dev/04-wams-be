package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.FileImageService;
import eu.isygoit.com.rest.service.impl.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Author;
import eu.isygoit.model.Template;
import eu.isygoit.model.schema.ComSchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.AuthorRepository;
import eu.isygoit.service.IAutherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = AuthorRepository.class)
@DmsLinkFileService(DmsLinkedFileService.class)
public class AuthorService extends FileImageService<Long, Author, AuthorRepository> implements IAutherService {
    private final AppProperties appProperties;

    public AuthorService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    @Override
    public String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }


    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Template.class.getSimpleName())
                .attribute(ComSchemaColumnConstantName.C_CODE)
                .prefix("AUTH")
                .valueLength(6L)
                .value(1L)
                .build();
    }


}
