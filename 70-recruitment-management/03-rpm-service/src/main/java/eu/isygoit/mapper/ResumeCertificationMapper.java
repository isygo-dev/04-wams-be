package eu.isygoit.mapper;

import eu.isygoit.dto.data.ResumeCertificationDto;
import eu.isygoit.model.ResumeCertification;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Resume certification mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface ResumeCertificationMapper extends EntityMapper<ResumeCertification, ResumeCertificationDto> {
}
