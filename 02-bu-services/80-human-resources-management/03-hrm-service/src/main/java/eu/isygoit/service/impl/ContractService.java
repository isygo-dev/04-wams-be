package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.exception.handler.EmployeeNotFoundException;
import eu.isygoit.exception.handler.LeaveSummaryNotFoundException;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Contract;
import eu.isygoit.model.Employee;
import eu.isygoit.model.LeaveSummary;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.ContractRepository;
import eu.isygoit.repository.EmployeeRepository;
import eu.isygoit.repository.LeaveSummaryRepository;
import eu.isygoit.service.IContractService;
import eu.isygoit.service.ILeaveSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * The type Contract service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = ContractRepository.class)
public class ContractService extends FileService<Long, Contract, ContractRepository> implements IContractService {

    private final AppProperties appProperties;
    private final ILeaveSummaryService leaveSummaryService;
    private final EmployeeRepository employeeRepository;
    private final LeaveSummaryRepository leaveSummaryRepository;

    /**
     * Instantiates a new Contract service.
     *
     * @param appProperties the app properties
     */
    @Autowired
    public ContractService(AppProperties appProperties, ILeaveSummaryService leaveSummaryService, EmployeeRepository employeeRepository, LeaveSummaryRepository leaveSummaryRepository) {
        this.appProperties = appProperties;
        this.leaveSummaryService = leaveSummaryService;
        this.employeeRepository = employeeRepository;
        this.leaveSummaryRepository = leaveSummaryRepository;
    }

    @Override
    public Contract afterUpdate(Contract contract) {
        // Retrieve employee and leave summary using Optional chaining
        Employee employee = employeeRepository.findById(contract.getEmployee())
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + contract.getEmployee()));

        LeaveSummary leaveSummaryUpdated = leaveSummaryRepository.findByCodeIgnoreCaseEmployeeAndYear(employee.getCode(),
                        String.valueOf(LocalDate.now().getYear()))
                .orElseThrow(() -> new LeaveSummaryNotFoundException("with employee Code: " + employee.getCode()));

        // Update leave summary if holiday information is provided
        if (contract.getHolidayInformation() != null) {
            leaveSummaryUpdated.setLeaveCount(contract.getHolidayInformation().getLegalLeaveCount());
            leaveSummaryUpdated.setRecoveryLeaveCount(contract.getHolidayInformation().getRecoveryLeaveCount());

            // Save or update leave summary
            leaveSummaryService.saveOrUpdate(leaveSummaryUpdated);
        }

        // Continue with the existing contract update logic
        return super.afterCreate(contract);
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Contract.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("CTR")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }

    @Override
    public Contract updateContractStatus(Long id, Boolean isLocked) {
        repository().updateContractStatus(isLocked, id);
        return repository().findById(id).orElse(null);
    }

    @Override
    public Contract beforeUpdate(Contract contract) {
        if (CollectionUtils.isEmpty(contract.getTags())) {
            contract.setTags(Arrays.asList("Contract" /*, contract.getType()*/));
        }
        return super.beforeUpdate(contract);
    }

    @Override
    public Contract beforeCreate(Contract contract) {
        if (CollectionUtils.isEmpty(contract.getTags())) {
            contract.setTags(Arrays.asList("Contract" /*, contract.getType()*/));
        }
        return super.beforeCreate(contract);
    }
}
