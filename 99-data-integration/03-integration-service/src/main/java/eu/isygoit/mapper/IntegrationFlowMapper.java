package eu.isygoit.mapper;

import eu.isygoit.dto.data.IntegrationFlowDto;
import eu.isygoit.model.IntegrationFlow;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Integration flow mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface IntegrationFlowMapper extends EntityMapper<IntegrationFlow, IntegrationFlowDto> {

}
