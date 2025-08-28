package eu.isygoit.repository;

import eu.isygoit.model.Resume;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Resume repository.
 */
@Repository
public interface ResumeRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<Resume, Long> {

    /**
     * Count by created by long.
     *
     * @param createdBy the created by
     * @return the long
     */
    Long countByCreatedBy(String createdBy);
}
