package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.InsuranceIdentityInfo;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.InsuranceIdentityInfoRepository;
import eu.isygoit.service.IInsuranceIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Insurance identity info service.
 */
@Slf4j
@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = InsuranceIdentityInfoRepository.class)
public class InsuranceIdentityInfoService extends ImageService<Long, InsuranceIdentityInfo, InsuranceIdentityInfoRepository> implements IInsuranceIdentityInfoService {

    private final AppProperties appProperties;

    /**
     * Instantiates a new Insurance identity info service.
     *
     * @param appProperties the app properties
     */
    public InsuranceIdentityInfoService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }


    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(InsuranceIdentityInfo.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("INS")
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
