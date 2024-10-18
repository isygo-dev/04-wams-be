package eu.isygoit.repository;

import eu.isygoit.model.Quiz;
import org.springframework.stereotype.Repository;


/**
 * The interface Quiz repository.
 */
@Repository
public interface QuizRepository extends JpaPagingAndSortingCodifiableRepository<Quiz, Long> {

}
