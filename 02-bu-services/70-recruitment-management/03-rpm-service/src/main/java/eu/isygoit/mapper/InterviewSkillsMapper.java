package eu.isygoit.mapper;

import eu.isygoit.dto.data.InterviewSkillsDto;
import eu.isygoit.model.InterviewSkills;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Interview skills mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface InterviewSkillsMapper extends EntityMapper<InterviewSkills, InterviewSkillsDto> {
}
