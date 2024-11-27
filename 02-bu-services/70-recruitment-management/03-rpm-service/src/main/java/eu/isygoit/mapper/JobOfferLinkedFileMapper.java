package eu.isygoit.mapper;

import eu.isygoit.dto.data.JobOfferLinkedFileDto;
import eu.isygoit.model.JobOfferLinkedFile;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Job offer linked file mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface JobOfferLinkedFileMapper extends EntityMapper<JobOfferLinkedFile, JobOfferLinkedFileDto> {
}
