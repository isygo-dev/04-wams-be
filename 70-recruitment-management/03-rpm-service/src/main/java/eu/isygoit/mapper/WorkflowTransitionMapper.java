package eu.isygoit.mapper;

import eu.isygoit.dto.data.WorkflowTransitionDto;
import eu.isygoit.model.WorkflowTransition;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Workflow transition mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface WorkflowTransitionMapper extends EntityMapper<WorkflowTransition, WorkflowTransitionDto> {
}
