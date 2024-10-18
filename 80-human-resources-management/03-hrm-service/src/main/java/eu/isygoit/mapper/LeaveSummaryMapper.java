package eu.isygoit.mapper;

import eu.isygoit.dto.data.LeaveSummaryDto;
import eu.isygoit.model.LeaveSummary;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Leave summary mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface LeaveSummaryMapper extends EntityMapper<LeaveSummary, LeaveSummaryDto> {

}
