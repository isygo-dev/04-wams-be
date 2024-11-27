package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IImageServiceMethods;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.EmployeeGlobalStatDto;
import eu.isygoit.dto.data.EmployeeStatDto;
import eu.isygoit.enums.IEnumBinaryStatus;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.model.Employee;
import eu.isygoit.model.EmployeeLinkedFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * The interface Employee service.
 */
public interface IEmployeeService extends ICrudServiceMethod<Long, Employee>, IImageServiceMethods<Long, Employee> {
    /**
     * Find employee by code employee.
     *
     * @param code the code
     * @return the employee
     */
    Employee findEmployeeByCode(String code);

    /**
     * Find employee by domain list.
     *
     * @param domain the domain
     * @return the list
     */
    List<Employee> findEmployeeByDomain(String domain);

    /**
     * Update employee status employee.
     *
     * @param id        the id
     * @param newStatus the new status
     * @return the employee
     */
    Employee updateEmployeeStatus(Long id, IEnumBinaryStatus.Types newStatus);

    /**
     * Upload additional file list.
     *
     * @param id    the id
     * @param files the files
     * @return the list
     * @throws IOException the io exception
     */
    List<EmployeeLinkedFile> uploadAdditionalFile(Long id, MultipartFile[] files) throws IOException;

    /**
     * Delete additional file boolean.
     *
     * @param parentId the parent id
     * @param fileId   the file id
     * @return the boolean
     * @throws IOException the io exception
     */
    boolean deleteAdditionalFile(Long parentId, Long fileId) throws IOException;

    /**
     * Gets object statistics.
     *
     * @param code           the code
     * @param requestContext the request context
     * @return the object statistics
     */
    EmployeeStatDto getObjectStatistics(String code, RequestContextDto requestContext);

    /**
     * Gets global statistics.
     *
     * @param statType       the stat type
     * @param requestContext the request context
     * @return the global statistics
     */
    EmployeeGlobalStatDto getGlobalStatistics(IEnumSharedStatType.Types statType, RequestContextDto requestContext);

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
