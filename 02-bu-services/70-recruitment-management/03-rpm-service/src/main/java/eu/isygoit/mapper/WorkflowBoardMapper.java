package eu.isygoit.mapper;

import eu.isygoit.dto.data.WorkflowBoardDto;
import eu.isygoit.model.WorkflowBoard;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Workflow board mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface WorkflowBoardMapper extends EntityMapper<WorkflowBoard, WorkflowBoardDto> {
}
