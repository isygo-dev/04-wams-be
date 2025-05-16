package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.api.EmployeeControllerApi;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.EmployeeDto;
import eu.isygoit.dto.data.MinEmployeeDto;
import eu.isygoit.enums.IEnumBinaryStatus;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.EmployeeMapper;
import eu.isygoit.model.Employee;
import eu.isygoit.service.IEmployeeService;
import eu.isygoit.service.impl.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Employee controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = EmployeeMapper.class, minMapper = EmployeeMapper.class, service = EmployeeService.class)
@RequestMapping(value = "/api/v1/private/employee")
public class EmployeeController extends MappedCrudController<Long, Employee, MinEmployeeDto, EmployeeDto, EmployeeService>
        implements EmployeeControllerApi {

    private final IEmployeeService employeeService;

    /**
     * Instantiates a new Employee controller.
     *
     * @param employeeService the employee service
     */
    public EmployeeController(EmployeeService employeeService) {
        super();
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployeeByCode(RequestContextDto requestContext,
                                                         String code) {
        try {
            return crudService().findByCode(code)
                    .map(employee -> ResponseEntity.ok(mapper().entityToDto(employee)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<List<EmployeeDto>> getEmployeeByDomain(RequestContextDto requestContext, String domain) {
        try {
            List<Employee> employees = crudService().findByDomain(domain);
            if (CollectionUtils.isEmpty(employees)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(mapper().listEntityToDto(employees));
        } catch (Exception e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    @Override
    public ResponseEntity<EmployeeDto> updateEmployeeStatus(RequestContextDto requestContext,
                                                            Long id,
                                                            IEnumBinaryStatus.Types newStatus) {
        log.info("update employee status");
        try {
            return ResponseFactory.ResponseOk(mapper().entityToDto(crudService().updateStatus(id, newStatus)));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public List<MinEmployeeDto> afterFindAll(RequestContextDto requestContext, List<MinEmployeeDto> employeeDtos) {
        return employeeDtos.stream()
                .map(employee -> {
                    String accountCode = crudService().getAccountCode(employee.getCode());
                    employee.setAccountCode(accountCode);
                    return employee;
                })
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Create account to employee response entity.
     *
     * @param requestContext the request context
     * @param employeeDto    the employee dto
     * @return the response entity
     */
    @PostMapping(path = "/create/account")
    public ResponseEntity<HttpStatus> createAccountToEmployee(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
            @Valid @RequestBody EmployeeDto employeeDto) {
        try {
            employeeService.createAccount(mapper().dtoToEntity(employeeDto));
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }
}
