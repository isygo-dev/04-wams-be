package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CodeAssignableService;
import eu.isygoit.constants.DomainConstants;
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
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = WorkflowTransitionRepository.class)
public class WorkflowTransitionService extends CodeAssignableService<Long, WorkflowTransition, WorkflowTransitionRepository>
        implements IWorkflowTransitionService {

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(WorkflowTransition.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFT")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }

}
