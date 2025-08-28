package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethods;
import eu.isygoit.com.rest.service.IImageServiceMethods;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.EmployeeGlobalStatDto;
import eu.isygoit.dto.data.EmployeeStatDto;
import eu.isygoit.enums.IEnumEnabledBinaryStatus;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.model.Employee;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The interface Employee service.
 */
public interface IEmployeeService extends ICrudServiceMethods<Long, Employee>, IImageServiceMethods<Long, Employee> {
    /**
     * Find employee by code employee.
     *
     * @param code the code
     * @return the employee
     */
    Optional<Employee> findByCode(String code);

    /**
     * Find employee by tenant list.
     *
     * @param tenant the tenant
     * @return the list
     */
    List<Employee> findByTenant(String tenant);

    /**
     * Update employee status employee.
     *
     * @param id        the id
     * @param newStatus the new status
     * @return the employee
     */
    Employee updateStatus(Long id, IEnumEnabledBinaryStatus.Types newStatus);

    /**
     * Gets object statistics.
     *
     * @param code           the code
     * @param requestContext the request context
     * @return the object statistics
     */
    EmployeeStatDto getObjectStatistics(String code, ContextRequestDto requestContext);

    /**
     * Gets global statistics.
     *
     * @param statType       the stat type
     * @param requestContext the request context
     * @return the global statistics
     */
    EmployeeGlobalStatDto getGlobalStatistics(IEnumSharedStatType.Types statType, ContextRequestDto requestContext);

    /**
     * Gets account code.
     *
     * @param employeeCode the employee code
     * @return the account code
     */
    String getAccountCode(String employeeCode);

    /**
     * Create account.
     *
     * @param employee the employee
     * @throws IOException the io exception
     */
    void createAccount(Employee employee) throws IOException;
}
