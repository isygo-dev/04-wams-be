package eu.isygoit.repository;

import eu.isygoit.model.CandidateQuizAnswer;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Candidate quiz answer repository.
 */
@Repository
public interface CandidateQuizAnswerRepository extends JpaPagingAndSortingRepository<CandidateQuizAnswer, Long> {

}
