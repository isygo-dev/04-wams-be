package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.WorkflowTransition;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.WorkflowTransitionRepository;
import eu.isygoit.service.IWorkflowTransitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = WorkflowTransitionRepository.class)
public class WorkflowTransitionService extends CodifiableService<Long, WorkflowTransition, WorkflowTransitionRepository>
        implements IWorkflowTransitionService {

    @Override
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(WorkflowTransition.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("WFT")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build());
    }

}
