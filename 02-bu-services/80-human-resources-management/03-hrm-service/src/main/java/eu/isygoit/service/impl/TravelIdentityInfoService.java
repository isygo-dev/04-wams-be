package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.TravelIdentityInfo;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TravelIdentityInfoRepository;
import eu.isygoit.service.ITravelIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * The type Travel identity info service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = TravelIdentityInfoRepository.class)
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
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(TravelIdentityInfo.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("TID")
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
