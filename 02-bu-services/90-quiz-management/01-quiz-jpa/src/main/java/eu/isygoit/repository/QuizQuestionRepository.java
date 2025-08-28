package eu.isygoit.repository;

import eu.isygoit.model.QuizQuestion;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Quiz repository.
 */
@Repository
public interface QuizQuestionRepository extends JpaPagingAndSortingRepository<QuizQuestion, Long> {

}
