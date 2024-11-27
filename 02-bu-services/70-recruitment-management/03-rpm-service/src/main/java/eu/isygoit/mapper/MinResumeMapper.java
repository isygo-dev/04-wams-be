package eu.isygoit.mapper;

import eu.isygoit.dto.data.MinResumeDto;
import eu.isygoit.model.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Min resume mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface MinResumeMapper extends EntityMapper<Resume, MinResumeDto> {
}
