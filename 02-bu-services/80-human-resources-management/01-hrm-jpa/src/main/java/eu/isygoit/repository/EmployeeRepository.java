package eu.isygoit.repository;

import eu.isygoit.enums.IEnumBinaryStatus;
import eu.isygoit.model.Employee;
import org.springframework.stereotype.Repository;


/**
 * The interface Employee repository.
 */
@Repository
public interface EmployeeRepository extends JpaPagingAndSortingSAASCodifiableRepository<Employee, Long> {

    /**
     * Count by domain ignore case and employee status long.
     *
     * @param senderDomain   the sender domain
     * @param employeeStatus the employee status
     * @return the long
     */
    Long countByDomainIgnoreCaseAndEmployeeStatus(String senderDomain, IEnumBinaryStatus.Types employeeStatus);
}
