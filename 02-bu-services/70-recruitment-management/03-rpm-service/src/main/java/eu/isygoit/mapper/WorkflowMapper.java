package eu.isygoit.mapper;

import eu.isygoit.dto.data.WorkflowDto;
import eu.isygoit.model.Workflow;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Workflow mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface WorkflowMapper extends EntityMapper<Workflow, WorkflowDto> {
}
