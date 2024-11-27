package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.api.IMappedMultiFileUploadApi;
import eu.isygoit.com.rest.controller.impl.MappedMultiFileController;
import eu.isygoit.dto.data.EmployeeDto;
import eu.isygoit.dto.data.EmployeeLinkedFileDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.EmployeeLinkedFileMapper;
import eu.isygoit.mapper.EmployeeMapper;
import eu.isygoit.mapper.EntityMapper;
import eu.isygoit.model.Employee;
import eu.isygoit.service.impl.EmployeeMultiFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Employee multi file controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/employee")
@CtrlDef(handler = HrmExceptionHandler.class, mapper = EmployeeMapper.class, minMapper = EmployeeMapper.class, service = EmployeeMultiFileService.class)
public class EmployeeMultiFileController extends MappedMultiFileController<Long, Employee, EmployeeLinkedFileDto, EmployeeDto, EmployeeDto, EmployeeMultiFileService>
        implements IMappedMultiFileUploadApi<EmployeeLinkedFileDto, Long> {

    @Autowired
    private EmployeeLinkedFileMapper employeeLinkedFileMapper;

    @Override
    public EntityMapper linkedFileMapper() {
        return employeeLinkedFileMapper;
    }
}
