package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.media.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.PersonalIdentityInfo;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.PersonalIdentityInfoRepository;
import eu.isygoit.service.IPersonalIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Personal identity info service.
 */
@Slf4j
@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = PersonalIdentityInfoRepository.class)
public class PersonalIdentityInfoService extends ImageService<Long, PersonalIdentityInfo, PersonalIdentityInfoRepository> implements IPersonalIdentityInfoService {

    private final AppProperties appProperties;


    /**
     * Instantiates a new Personal identity info service.
     *
     * @param appProperties the app properties
     */
    public PersonalIdentityInfoService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }


    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(PersonalIdentityInfo.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("NID")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }

    @Override
    protected String getUploadDirectory() {
        return appProperties.getUploadDirectory();
    }

}
