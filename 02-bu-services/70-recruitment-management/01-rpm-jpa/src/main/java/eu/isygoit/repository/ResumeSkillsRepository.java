package eu.isygoit.repository;

import eu.isygoit.model.ResumeSkills;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Resume skills repository.
 */
@Repository
public interface ResumeSkillsRepository extends JpaPagingAndSortingRepository<ResumeSkills, Long> {

    /**
     * Find distinct skill names list.
     *
     * @return the list
     */
    @Query("select distinct rs.name from ResumeSkills rs")
    List<String> findDistinctSkillNames();
}
