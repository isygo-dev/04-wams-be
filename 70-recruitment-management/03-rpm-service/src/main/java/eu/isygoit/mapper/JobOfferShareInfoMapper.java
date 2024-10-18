package eu.isygoit.mapper;

import eu.isygoit.dto.data.JobOfferShareInfoDto;
import eu.isygoit.model.JobOfferShareInfo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Job offer share info mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface JobOfferShareInfoMapper extends EntityMapper<JobOfferShareInfo, JobOfferShareInfoDto> {
}
