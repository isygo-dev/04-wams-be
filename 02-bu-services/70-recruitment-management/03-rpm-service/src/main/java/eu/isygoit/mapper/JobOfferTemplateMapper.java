package eu.isygoit.mapper;

import eu.isygoit.dto.data.JobOfferTemplateDto;
import eu.isygoit.model.JobOfferTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Job offer template mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface JobOfferTemplateMapper extends EntityMapper<JobOfferTemplate, JobOfferTemplateDto> {
}

