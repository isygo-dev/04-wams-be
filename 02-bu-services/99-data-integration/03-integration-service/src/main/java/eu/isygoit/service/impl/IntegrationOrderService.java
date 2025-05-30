package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.IntegrationOrder;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.IntegrationOrderRepository;
import eu.isygoit.service.IIntegrationOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Integration order service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = IntegrationOrderRepository.class)
public class IntegrationOrderService extends FileService<Long, IntegrationOrder, IntegrationOrderRepository>
        implements IIntegrationOrderService {

    private final AppProperties appProperties;

    /**
     * Instantiates a new Integration order service.
     *
     * @param appProperties the app properties
     */
    public IntegrationOrderService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(IntegrationOrder.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("INO")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }
}
