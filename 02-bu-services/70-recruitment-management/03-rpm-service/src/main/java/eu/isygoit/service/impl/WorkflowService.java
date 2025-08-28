package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectRepository;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.TenantConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.repository.code.NextCodeRepository;
import eu.isygoit.model.Workflow;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.WorkflowRepository;
import eu.isygoit.service.IWorkflowService;
import eu.isygoit.service.IWorkflowStateService;
import eu.isygoit.service.IWorkflowTransitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Transactional
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = WorkflowRepository.class)
public class WorkflowService extends CodeAssignableService<Long, Workflow, WorkflowRepository> implements IWorkflowService {

    @Autowired
    private IWorkflowStateService workflowStateService;
    @Autowired
    private IWorkflowTransitionService workflowTransitionService;

    @Override
    public Workflow beforeCreate(Workflow workflow) {
        if (!CollectionUtils.isEmpty(workflow.getWorkflowStates())) {
            workflow.setWorkflowStates(this.workflowStateService.createBatch(workflow.getWorkflowStates()));
        }

        if (!CollectionUtils.isEmpty(workflow.getWorkflowTransitions())) {
            workflow.setWorkflowTransitions(this.workflowTransitionService.createBatch(workflow.getWorkflowTransitions()));
        }

        return workflow;
    }

    @Override
    public Workflow beforeUpdate(Workflow workflow) {
        if (!CollectionUtils.isEmpty(workflow.getWorkflowStates())) {
            workflow.setWorkflowStates(this.workflowStateService.saveOrUpdate(workflow.getWorkflowStates()));
        }

        if (!CollectionUtils.isEmpty(workflow.getWorkflowTransitions())) {
            workflow.setWorkflowTransitions(this.workflowTransitionService.saveOrUpdate(workflow.getWorkflowTransitions()));
        }
        return workflow;
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(Workflow.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFL")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }


    @Override
    public List<String> getWorkflowNotAssociated() {
        return repository().findWorkflowNotAssociated();
    }
}
