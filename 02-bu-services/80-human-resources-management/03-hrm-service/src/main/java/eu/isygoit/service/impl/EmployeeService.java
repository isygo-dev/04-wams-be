package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.async.kafka.KafkaRegisterAccountProducer;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.service.impl.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.EmployeeGlobalStatDto;
import eu.isygoit.dto.data.EmployeeStatDto;
import eu.isygoit.dto.request.NewAccountDto;
import eu.isygoit.enums.IEnumAccountOrigin;
import eu.isygoit.enums.IEnumBinaryStatus;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.exception.StatisticTypeNotSupportedException;
import eu.isygoit.model.*;
import eu.isygoit.model.extendable.NextCodeModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.ims.ImAccountService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.AssoAccountEmployeeRepository;
import eu.isygoit.repository.ContractRepository;
import eu.isygoit.repository.EmployeeLinkedFileRepository;
import eu.isygoit.repository.EmployeeRepository;
import eu.isygoit.service.IEmployeeService;
import eu.isygoit.service.ILeaveSummaryService;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The type Employee service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = EmployeeRepository.class)
@DmsLinkFileService(DmsLinkedFileService.class)
public class EmployeeService extends ImageService<Long, Employee, EmployeeRepository> implements IEmployeeService {

    private final AppProperties appProperties;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ILeaveSummaryService leaveSummaryService;
    @Autowired
    private KafkaRegisterAccountProducer kafkaRegisterAccountProducer;
    @Autowired
    private DmsLinkedFileService dmsLinkedFileService;
    @Autowired
    private EmployeeLinkedFileRepository employeeLinkedFileRepository;
    @Autowired
    private ImAccountService imAccountService;
    @Autowired
    private AssoAccountEmployeeRepository assoAccountEmployeeRepository;

    /**
     * Instantiates a new Employee service.
     *
     * @param appProperties the app properties
     */
    public EmployeeService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public Employee beforeUpdate(Employee employee) {
        Optional<Employee> optional = repository().findById(employee.getId());
        if (optional.isPresent()) {
            employee.setContracts(optional.get().getContracts());
        }
        return super.beforeUpdate(employee);
    }

    @Override
    public Employee afterCreate(Employee employee) {
        leaveSummaryService.create(LeaveSummary.builder().codeEmployee(employee.getCode())
                .year(String.valueOf(LocalDate.now().getYear()))
                .nameEmployee(employee.getFirstName() + ' ' + employee.getLastName())
                .build());

        try {
            if (employee.getIsLinkedToUser()) {
                createAccount(employee);
            }
        } catch (IOException e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
        }
        return super.afterCreate(employee);
    }

    @Override
    public void afterDelete(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            List<Contract> contracts = employee.getContracts();
            for (Contract contract : contracts) {
                contractRepository.delete(contract);
            }
        }
        super.afterDelete(id);
    }

    @Override
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Employee.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("EMP")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build());
    }

    @Override
    protected String getUploadDirectory() {
        return appProperties.getUploadDirectory();
    }

    @Override
    public Employee findEmployeeByCode(String code) {
        Optional<Employee> optionalEmployee = employeeRepository.findByCodeIgnoreCase(code);
        if (optionalEmployee.isPresent()) {
            return optionalEmployee.get();
        } else {
            throw new NotFoundException("Employee not found with CODE: " + code);
        }
    }

    @Override
    public List<Employee> findEmployeeByDomain(String domain) {
        List<Employee> employees = employeeRepository.findByDomainIgnoreCaseIn(Arrays.asList(domain));
        if (!employees.isEmpty()) {
            return employees;
        } else {
            throw new NotFoundException("Employee not found with domain: " + domain);
        }
    }

    @Override
    public Employee updateEmployeeStatus(Long id, IEnumBinaryStatus.Types newStatus) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        //TODO pour assurer notifications
        if (optionalEmployee.isPresent()) {
            optionalEmployee.get().setEmployeeStatus(newStatus);
        } else {
            throw new NotFoundException("Employee not found with CODE: " + id);
        }
        return null;
    }

    @Override
    public EmployeeStatDto getObjectStatistics(String code, RequestContextDto requestContext) {
        return EmployeeStatDto.builder().contractCount(getTotalContractByEmployee(code))
                .activeContractEndDate(getLastContractEndDate(code))
                .activeContractAnniversaryDate(getActiveContractAnniversaryDate(code))
                .nextBonusDate(getLastDueDate(code))
                .build();
    }

    private Integer getTotalContractByEmployee(String code) {
        Optional<Employee> optionalEmployee = employeeRepository.findByCodeIgnoreCase(code);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            List<Contract> contracts = employee.getContracts();
            return contracts.size();
        } else {
            return 0;
        }
    }

    private LocalDate getLastContractEndDate(String code) {
        Optional<Employee> optionalEmployee = employeeRepository.findByCodeIgnoreCase(code);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            if (!CollectionUtils.isEmpty(employee.getContracts())) {
                return employee.getContracts().stream()
                        .filter(contract -> contract.getEndDate() != null)
                        .map(Contract::getEndDate)
                        .max(LocalDate::compareTo)
                        .orElse(null);
            }
        }
        return null;
    }

    private LocalDate getActiveContractAnniversaryDate(String code) {
        Optional<Employee> optionalEmployee = employeeRepository.findByCodeIgnoreCase(code);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            if (!CollectionUtils.isEmpty(employee.getContracts())) {
                Optional<Contract> activeContract = employee.getContracts().stream()
                        .filter(contract -> contract.getEndDate() == null || contract.getEndDate().isAfter(LocalDate.now()))
                        .findFirst();

                if (activeContract.isPresent() && activeContract.get().getStartDate() != null) {
                    LocalDate startLocalDate = activeContract.get().getStartDate();
                    int yearsBetween = Period.between(startLocalDate, LocalDate.now()).getYears();
                    LocalDate nextAnniversaryDate = startLocalDate.plusYears(yearsBetween + 1L);
                    return nextAnniversaryDate;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Gets last due date.
     *
     * @param code the code
     * @return the last due date
     */
    public LocalDate getLastDueDate(String code) {
        Optional<Employee> employeeOpt = employeeRepository.findByCodeIgnoreCase(code);
        if (!employeeOpt.isPresent()) {
            return null;
        }
        Employee employee = employeeOpt.get();

        // Fetch contracts for the employee
        List<Contract> contracts = employee.getContracts();

        // Get the last active contract
        Optional<Contract> lastActiveContractOpt = contracts.stream()
                .filter(contract -> !contract.getCheckCancel()) // Filter out canceled contracts
                .max(Comparator.comparing(Contract::getEndDate)); // Get the contract with the latest end date

        if (!lastActiveContractOpt.isPresent()) {
            return null;
        }
        Contract lastActiveContract = lastActiveContractOpt.get();

        SalaryInformation salaryInformation = lastActiveContract.getSalaryInformation();
        if (salaryInformation == null) {
            return null;
        }
        List<Prime> primes = salaryInformation.getPrimes();
        return primes.stream()
                .flatMap(prime -> prime.getBonusSchedules().stream())
                .map(PaymentBonusSchedule::getDueDate)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    @Override
    public EmployeeGlobalStatDto getGlobalStatistics(IEnumSharedStatType.Types statType, RequestContextDto requestContext) {
        EmployeeGlobalStatDto.EmployeeGlobalStatDtoBuilder builder = EmployeeGlobalStatDto.builder();
        switch (statType) {
            case TOTAL_COUNT:
                builder.totalCount(stat_GetEmployeesCount(requestContext));
                break;
            case ACTIVE_COUNT:
                builder.activeCount(stat_GetActiveEmployeesCount(requestContext));
                break;
            case CONFIRMED_COUNT:
                builder.confirmedCount(stat_GetConfirmedEmployeesCount(requestContext));
                break;
            default:
                throw new StatisticTypeNotSupportedException(statType.name());
        }

        return builder.build();
    }

    @Override
    public String getAccountCode(String employeeCode) {
        Optional<AssoAccountEmployee> assoAccountEmployee = assoAccountEmployeeRepository.findByEmployee_Code(employeeCode);
        return assoAccountEmployee.isPresent() ? assoAccountEmployee.get().getEmployee().getCode() : null;
    }

    @Override
    public void createAccount(Employee employee) throws IOException {
        kafkaRegisterAccountProducer.sendMessage(NewAccountDto.builder()
                .origin(new StringBuilder(IEnumAccountOrigin.Types.EMPLOYEE.name()).append("-").append(employee.getCode()).toString())
                .domain(employee.getDomain())
                .email(employee.getEmail())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .phoneNumber(employee.getPhone())
                .functionRole(employee.getFunctionRole())
                .build());
    }

    private Long stat_GetEmployeesCount(RequestContextDto requestContext) {
        if (DomainConstants.SUPER_DOMAIN_NAME.equals(requestContext.getSenderDomain())) {
            return repository().count();
        } else {
            return repository().countByDomainIgnoreCase(requestContext.getSenderDomain());
        }
    }

    private Long stat_GetConfirmedEmployeesCount(RequestContextDto requestContext) {
        ResponseEntity<Long> responseEntity = imAccountService.getConfirmedAccountNumberByEmployee(requestContext);
        return responseEntity.getBody();
    }

    private Long stat_GetActiveEmployeesCount(RequestContextDto requestContext) {
        return repository().countByDomainIgnoreCaseAndEmployeeStatus(requestContext.getSenderDomain(), IEnumBinaryStatus.Types.ENABLED);
    }
}
