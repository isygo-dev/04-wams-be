package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.exception.handler.LeaveSummaryNotFoundException;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Contract;
import eu.isygoit.model.Employee;
import eu.isygoit.model.LeaveSummary;
import eu.isygoit.model.extendable.NextCodeModel;
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
import java.util.Optional;

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
    @Autowired
    private ILeaveSummaryService leaveSummaryService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveSummaryRepository leaveSummaryRepository;

    /**
     * Instantiates a new Contract service.
     *
     * @param appProperties the app properties
     */
    public ContractService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public Contract afterUpdate(Contract contract) {
        Optional<Employee> optional = employeeRepository.findById(contract.getEmployee());
        if (optional.isPresent()) {
            Optional<LeaveSummary> leaveSummary = leaveSummaryRepository.findByCodeIgnoreCaseEmployeeAndYear(optional.get().getCode(),
                    String.valueOf(LocalDate.now().getYear()));
            if (leaveSummary.isPresent()) {
                LeaveSummary leaveSummaryUpdated = leaveSummary.get();
                if (contract.getHolidayInformation() != null) {
                    leaveSummaryUpdated.setLeaveCount(contract.getHolidayInformation().getLegalLeaveCount());
                    leaveSummaryUpdated.setRecoveryLeaveCount(contract.getHolidayInformation().getRecoveryLeaveCount());
                }

                leaveSummaryService.saveOrUpdate(leaveSummaryUpdated);
            } else {
                throw new LeaveSummaryNotFoundException("with employee Code: " + optional.get().getCode());
            }
        }
        return super.afterCreate(contract);
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public Optional<NextCodeModel> initCodeGenerator() {
        return Optional.ofNullable(AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Contract.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("CTR")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build());
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
