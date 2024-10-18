package eu.isygoit.mapper;

import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.model.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Quiz mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface QuizMapper extends EntityMapper<Quiz, QuizDto> {

}
