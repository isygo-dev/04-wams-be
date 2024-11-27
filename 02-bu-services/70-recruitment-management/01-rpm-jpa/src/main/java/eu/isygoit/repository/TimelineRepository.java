package eu.isygoit.repository;

import eu.isygoit.model.Timeline;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Timeline repository.
 */
@Repository
public interface TimelineRepository extends JpaPagingAndSortingSAASCodifiableRepository<Timeline, Long> {

    /**
     * Find all by domain ignore case and code ignore case list.
     *
     * @param domain the domain
     * @param code   the code
     * @return the list
     */
    List<Timeline> findAllByDomainIgnoreCaseAndCodeIgnoreCase(String domain, String code);
}
