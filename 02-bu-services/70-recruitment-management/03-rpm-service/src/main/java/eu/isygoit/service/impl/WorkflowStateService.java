package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.WorkflowState;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.WorkflowStateRepository;
import eu.isygoit.service.IWorkflowStateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = WorkflowStateRepository.class)
public class WorkflowStateService extends CodeAssignableService<Long, WorkflowState, WorkflowStateRepository>
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
