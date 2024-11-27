package eu.isygoit.mapper;

import eu.isygoit.dto.data.CandidateQuizDto;
import eu.isygoit.model.CandidateQuiz;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Candidate quiz mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface CandidateQuizMapper extends EntityMapper<CandidateQuiz, CandidateQuizDto> {

}
