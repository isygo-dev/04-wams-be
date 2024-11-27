package eu.isygoit.repository;

import eu.isygoit.model.LeaveSummary;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * The interface Leave summary repository.
 */
@Repository
public interface LeaveSummaryRepository extends JpaPagingAndSortingRepository<LeaveSummary, Long> {

    /**
     * Find by code ignore case employee and year optional.
     *
     * @param code the code
     * @param year the year
     * @return the optional
     */
    Optional<LeaveSummary> findByCodeIgnoreCaseEmployeeAndYear(String code, String year);
}
