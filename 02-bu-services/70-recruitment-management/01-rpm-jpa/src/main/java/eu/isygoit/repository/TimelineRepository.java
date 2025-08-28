package eu.isygoit.repository;

import eu.isygoit.model.Timeline;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Timeline repository.
 */
@Repository
public interface TimelineRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<Timeline, Long> {

    /**
     * Find all by tenant ignore case and code ignore case list.
     *
     * @param tenant the tenant
     * @param code   the code
     * @return the list
     */
    List<Timeline> findAllByTenantIgnoreCaseAndCodeIgnoreCase(String tenant, String code);
}
