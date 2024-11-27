package eu.isygoit.repository;

import eu.isygoit.model.ResumeEducation;
import org.springframework.stereotype.Repository;

/**
 * The interface Resume education repository.
 */
@Repository
public interface ResumeEducationRepository extends JpaPagingAndSortingRepository<ResumeEducation, Long> {
}
