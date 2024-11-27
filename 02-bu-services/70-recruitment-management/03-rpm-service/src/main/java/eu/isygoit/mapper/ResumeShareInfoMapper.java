package eu.isygoit.mapper;

import eu.isygoit.dto.data.ResumeShareInfoDto;
import eu.isygoit.model.ResumeShareInfo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Resume share info mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface ResumeShareInfoMapper extends EntityMapper<ResumeShareInfo, ResumeShareInfoDto> {
}
