package eu.isygoit.service.impl;

import eu.isygoit.annotation.*;
import eu.isygoit.com.rest.service.media.MultiFileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.constants.TenantConstants;
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
@InjectDmsLinkedFileService(DmsLinkedFileService.class)
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = EmployeeRepository.class)
@InjectLinkedFileRepository(value = EmployeeLinkedFileRepository.class)
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
                .tenant(TenantConstants.DEFAULT_TENANT_NAME)
                .entity(Employee.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("EMF")
                .valueLength(6L)
                .codeValue(1L)
                .increment(1)
                .build();
    }
}
