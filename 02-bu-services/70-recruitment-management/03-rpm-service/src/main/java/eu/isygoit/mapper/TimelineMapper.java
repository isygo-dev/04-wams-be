package eu.isygoit.mapper;

import eu.isygoit.dto.data.TimelineDto;
import eu.isygoit.model.Timeline;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Timeline mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface TimelineMapper extends EntityMapper<Timeline, TimelineDto> {
}
