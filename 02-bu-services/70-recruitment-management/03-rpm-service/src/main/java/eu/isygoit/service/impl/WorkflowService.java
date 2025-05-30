package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
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
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = WorkflowRepository.class)
public class WorkflowService extends CodeAssignableService<Long, Workflow, WorkflowRepository> implements IWorkflowService {

    @Autowired
    private IWorkflowStateService workflowStateService;
    @Autowired
    private IWorkflowTransitionService workflowTransitionService;

    @Override
    public Workflow beforeCreate(Workflow workflow) {
        if (!CollectionUtils.isEmpty(workflow.getWorkflowStates())) {
            workflow.setWorkflowStates(this.workflowStateService.create(workflow.getWorkflowStates()));
        }

        if (!CollectionUtils.isEmpty(workflow.getWorkflowTransitions())) {
            workflow.setWorkflowTransitions(this.workflowTransitionService.create(workflow.getWorkflowTransitions()));
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
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Workflow.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFL")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }


    @Override
    public List<String> getWorkflowNotAssociated() {
        return repository().findWorkflowNotAssociated();
    }
}
