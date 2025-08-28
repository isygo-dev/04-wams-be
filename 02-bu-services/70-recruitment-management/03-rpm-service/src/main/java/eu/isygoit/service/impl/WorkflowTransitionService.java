package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.WorkflowTransition;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.WorkflowTransitionRepository;
import eu.isygoit.service.IWorkflowTransitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = WorkflowTransitionRepository.class)
public class WorkflowTransitionService extends CodeAssignableService<Long, WorkflowTransition, WorkflowTransitionRepository>
        implements IWorkflowTransitionService {

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(WorkflowTransition.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFT")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }

}
