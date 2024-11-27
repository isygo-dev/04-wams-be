package eu.isygoit.mapper;

import eu.isygoit.dto.data.JobOfferApplicationDto;
import eu.isygoit.model.JobOfferApplication;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Job offer application mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface JobOfferApplicationMapper extends EntityMapper<JobOfferApplication, JobOfferApplicationDto> {
}
