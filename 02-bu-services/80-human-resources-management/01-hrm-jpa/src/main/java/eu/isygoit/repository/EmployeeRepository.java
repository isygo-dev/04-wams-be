package eu.isygoit.repository;

import eu.isygoit.enums.IEnumEnabledBinaryStatus;
import eu.isygoit.model.Employee;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Employee repository.
 */
@Repository
public interface EmployeeRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<Employee, Long> {

    /**
     * Count by tenant ignore case and employee status long.
     *
     * @param senderTenant   the sender tenant
     * @param employeeStatus the employee status
     * @return the long
     */
    Long countByTenantIgnoreCaseAndEmployeeStatus(String senderTenant, IEnumEnabledBinaryStatus.Types employeeStatus);
}
