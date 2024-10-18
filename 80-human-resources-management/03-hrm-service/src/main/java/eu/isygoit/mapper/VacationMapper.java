package eu.isygoit.mapper;

import eu.isygoit.dto.data.VacationDto;
import eu.isygoit.model.Vacation;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Vacation mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface VacationMapper extends EntityMapper<Vacation, VacationDto> {

}
