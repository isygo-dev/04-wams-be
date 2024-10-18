package eu.isygoit.repository;

import eu.isygoit.model.QuizQuestion;
import org.springframework.stereotype.Repository;


/**
 * The interface Quiz repository.
 */
@Repository
public interface QuizQuestionRepository extends JpaPagingAndSortingRepository<QuizQuestion, Long> {

}
