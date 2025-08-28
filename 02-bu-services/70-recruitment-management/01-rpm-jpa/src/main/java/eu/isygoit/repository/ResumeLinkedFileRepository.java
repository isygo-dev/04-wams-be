package eu.isygoit.repository;

import eu.isygoit.model.ResumeLinkedFile;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Resume linked file repository.
 */
@Repository
public interface ResumeLinkedFileRepository extends JpaPagingAndSortingCodeAssingnableRepository<ResumeLinkedFile, Long> {
}
