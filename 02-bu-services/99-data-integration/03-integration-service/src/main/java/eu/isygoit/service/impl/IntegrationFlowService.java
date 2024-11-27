package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.IntegrationFlow;
import eu.isygoit.model.nosql.IntegrationElement;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.IntegrationElementRepository;
import eu.isygoit.repository.IntegrationFlowRepository;
import eu.isygoit.service.IIntegrationFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Integration flow service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = IntegrationFlowRepository.class)
public class IntegrationFlowService extends FileService<Long, IntegrationFlow, IntegrationFlowRepository>
        implements IIntegrationFlowService {

    private final AppProperties appProperties;

    @Autowired
    private IntegrationElementRepository integrationElementRepository;

    /**
     * Instantiates a new Integration flow service.
     *
     * @param appProperties the app properties
     */
    public IntegrationFlowService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(IntegrationFlow.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("INF")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public List<IntegrationElement> findAllIntegratedElements(Long flowId) {
        return integrationElementRepository.findByFlowId(flowId);
    }

    public IntegrationElement findIntegratedElementById(Long elementId) {
        return integrationElementRepository.findById(elementId).orElse(null);
    }
}
