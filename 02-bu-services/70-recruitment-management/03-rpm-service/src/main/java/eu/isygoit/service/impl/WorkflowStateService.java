package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.WorkflowState;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.WorkflowStateRepository;
import eu.isygoit.service.IWorkflowStateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Workflow state service.
 */
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = WorkflowStateRepository.class)
public class WorkflowStateService extends CodifiableService<Long, WorkflowState, WorkflowStateRepository>
        implements IWorkflowStateService {

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(WorkflowState.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFS")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }
}
