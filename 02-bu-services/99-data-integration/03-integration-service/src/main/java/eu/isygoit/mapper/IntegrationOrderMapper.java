package eu.isygoit.mapper;

import eu.isygoit.dto.data.IntegrationOrderDto;
import eu.isygoit.model.IntegrationOrder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Integration order mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface IntegrationOrderMapper extends EntityMapper<IntegrationOrder, IntegrationOrderDto> {

}
