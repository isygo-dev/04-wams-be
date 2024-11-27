package eu.isygoit.mapper;

import eu.isygoit.dto.data.ResumeDto;
import eu.isygoit.model.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Resume mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface ResumeMapper extends EntityMapper<Resume, ResumeDto> {
}
