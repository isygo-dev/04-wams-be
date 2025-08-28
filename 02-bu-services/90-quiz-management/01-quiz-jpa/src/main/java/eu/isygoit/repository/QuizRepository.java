package eu.isygoit.repository;

import eu.isygoit.model.Quiz;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * The interface Quiz repository.
 */
@Repository
public interface QuizRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<Quiz, Long> {

    /**
     * Find by tags in list.
     *
     * @param tags the tags
     * @return the list
     */
    List<Quiz> findByTagsIn(List<String> tags);

    /**
     * Find by category in list.
     *
     * @param categories the categories
     * @return the list
     */
    List<Quiz> findByCategoryIn(List<String> categories);
}
