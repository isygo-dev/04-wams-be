package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedImageController;
import eu.isygoit.dto.data.EmployeeDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.EmployeeMapper;
import eu.isygoit.model.Employee;
import eu.isygoit.service.impl.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Employee image controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = EmployeeMapper.class, minMapper = EmployeeMapper.class, service = EmployeeService.class)
@RequestMapping(value = "/api/v1/private/employee")
public class EmployeeImageController extends MappedImageController<Long, Employee, EmployeeDto, EmployeeDto, EmployeeService> {

}
