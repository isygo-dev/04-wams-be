package eu.isygoit.mapper;

import eu.isygoit.dto.data.QuizQuestionDto;
import eu.isygoit.model.QuizQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Quiz question mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface QuizQuestionMapper extends EntityMapper<QuizQuestion, QuizQuestionDto> {

}
