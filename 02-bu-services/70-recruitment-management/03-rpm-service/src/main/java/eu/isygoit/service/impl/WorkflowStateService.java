package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.TenantConstants;
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
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = WorkflowStateRepository.class)
public class WorkflowStateService extends CodeAssignableService<Long, WorkflowState, WorkflowStateRepository>
        implements IWorkflowStateService {

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(WorkflowState.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFS")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }
}
