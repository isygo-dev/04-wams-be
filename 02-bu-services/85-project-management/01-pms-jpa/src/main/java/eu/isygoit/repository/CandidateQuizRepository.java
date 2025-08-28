package eu.isygoit.repository;

import eu.isygoit.model.CandidateQuiz;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Candidate quiz repository.
 */
@Repository
public interface CandidateQuizRepository extends JpaPagingAndSortingCodeAssingnableRepository<CandidateQuiz, Long> {

}
