package eu.isygoit.repository;

import eu.isygoit.model.ResumeProfExperience;
import org.springframework.stereotype.Repository;

/**
 * The interface Resume prof experience repository.
 */
@Repository
public interface ResumeProfExperienceRepository extends JpaPagingAndSortingRepository<ResumeProfExperience, Long> {
}
