package eu.isygoit.repository;

import eu.isygoit.model.JobOfferLinkedFile;
import org.springframework.stereotype.Repository;

/**
 * The interface Job offer linked file repository.
 */
@Repository
public interface JobOfferLinkedFileRepository extends JpaPagingAndSortingCodifiableRepository<JobOfferLinkedFile, Long> {
}
