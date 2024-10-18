package eu.isygoit.mapper;

import eu.isygoit.dto.data.WorkflowStateDto;
import eu.isygoit.model.WorkflowState;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Workflow state mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface WorkflowStateMapper extends EntityMapper<WorkflowState, WorkflowStateDto> {
}
