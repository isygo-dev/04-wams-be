package eu.isygoit.mapper;

import eu.isygoit.dto.data.JobOfferDto;
import eu.isygoit.model.JobOffer;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Job offer mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface JobOfferMapper extends EntityMapper<JobOffer, JobOfferDto> {
}
