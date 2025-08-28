package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.repository.code.NextCodeRepository;
import eu.isygoit.model.TravelIdentityInfo;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TravelIdentityInfoRepository;
import eu.isygoit.service.ITravelIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Travel identity info service.
 */
@Slf4j
@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = TravelIdentityInfoRepository.class)
public class TravelIdentityInfoService extends ImageService<Long, TravelIdentityInfo, TravelIdentityInfoRepository> implements ITravelIdentityInfoService {

    private final AppProperties appProperties;

    /**
     * Instantiates a new Travel identity info service.
     *
     * @param appProperties the app properties
     */
    public TravelIdentityInfoService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(TravelIdentityInfo.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("TID")
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
