package eu.isygoit.service.impl;

import eu.isygoit.annotation.*;
import eu.isygoit.com.rest.service.MultiFileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.Employee;
import eu.isygoit.model.EmployeeLinkedFile;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.EmployeeLinkedFileRepository;
import eu.isygoit.repository.EmployeeRepository;
import eu.isygoit.service.IEmployeeMultiFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Employee multi file service.
 */
@Slf4j
@Service
@Transactional
@DmsLinkFileService(DmsLinkedFileService.class)
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = EmployeeRepository.class)
@ServLinkFileRepo(value = EmployeeLinkedFileRepository.class)
public class EmployeeMultiFileService extends MultiFileService<Long, Employee, EmployeeLinkedFile, EmployeeRepository, EmployeeLinkedFileRepository>
        implements IEmployeeMultiFileService {

    private final AppProperties appProperties;

    /**
     * Instantiates a new Employee multi file service.
     *
     * @param appProperties the app properties
     */
    public EmployeeMultiFileService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Employee.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("EMF")
                .valueLength(6L)
                .value(1L)
                .increment(1)
                .build();
    }
}
