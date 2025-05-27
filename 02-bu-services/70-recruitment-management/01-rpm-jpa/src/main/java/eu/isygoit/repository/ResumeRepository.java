package eu.isygoit.repository;

import eu.isygoit.model.Resume;
import org.springframework.stereotype.Repository;

/**
 * The interface Resume repository.
 */
@Repository
public interface ResumeRepository extends JpaPagingAndSortingSAASCodifiableRepository<Resume, Long> {

    /**
     * Count by created by long.
     *
     * @param createdBy the created by
     * @return the long
     */
    Long countByCreatedBy(String createdBy);
}
