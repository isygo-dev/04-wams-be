package eu.isygoit.mapper;

import eu.isygoit.dto.data.IntegrationFlowFileDto;
import eu.isygoit.model.IntegrationFlow;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Integration flow file mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface IntegrationFlowFileMapper extends EntityMapper<IntegrationFlow, IntegrationFlowFileDto> {

}
