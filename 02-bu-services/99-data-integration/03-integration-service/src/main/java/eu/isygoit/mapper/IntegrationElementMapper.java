package eu.isygoit.mapper;

import eu.isygoit.dto.data.IntegrationElementDto;
import eu.isygoit.model.nosql.IntegrationElement;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;


/**
 * The interface Integration element mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface IntegrationElementMapper extends EntityMapper<IntegrationElement, IntegrationElementDto> {

}
