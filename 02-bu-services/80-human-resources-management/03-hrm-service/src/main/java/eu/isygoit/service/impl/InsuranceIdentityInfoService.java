package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.InsuranceIdentityInfo;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.InsuranceIdentityInfoRepository;
import eu.isygoit.service.IInsuranceIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * The type Insurance identity info service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = InsuranceIdentityInfoRepository.class)
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
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(InsuranceIdentityInfo.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("INS")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build());
    }

    @Override
    protected String getUploadDirectory() {
        return appProperties.getUploadDirectory();
    }
}
