package eu.isygoit.mapper;

import eu.isygoit.dto.data.IntegrationOrderFileDto;
import eu.isygoit.model.IntegrationOrder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Integration order file mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface IntegrationOrderFileMapper extends EntityMapper<IntegrationOrder, IntegrationOrderFileDto> {

}
