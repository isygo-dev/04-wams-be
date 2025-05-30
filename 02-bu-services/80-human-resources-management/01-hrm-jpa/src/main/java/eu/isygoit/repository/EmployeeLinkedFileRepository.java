package eu.isygoit.repository;

import eu.isygoit.model.EmployeeLinkedFile;
import org.springframework.stereotype.Repository;

/**
 * The interface Employee linked file repository.
 */
@Repository
public interface EmployeeLinkedFileRepository extends JpaPagingAndSortingCodeAssingnableRepository<EmployeeLinkedFile, Long> {
}
