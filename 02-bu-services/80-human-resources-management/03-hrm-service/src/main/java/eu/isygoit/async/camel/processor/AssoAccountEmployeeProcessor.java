package eu.isygoit.async.camel.processor;

import eu.isygoit.com.camel.processor.AbstractStringProcessor;
import eu.isygoit.dto.data.AssoAccountDto;
import eu.isygoit.helper.JsonHelper;
import eu.isygoit.model.AssoAccountEmployee;
import eu.isygoit.model.Employee;
import eu.isygoit.repository.AssoAccountEmployeeRepository;
import eu.isygoit.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * The type Asso account employee processor.
 */
@Slf4j
@Component
@Qualifier("assoAccountEmployeeProcessor")
public class AssoAccountEmployeeProcessor extends AbstractStringProcessor {
    /**
     * The Employee service.
     */
    @Autowired
    IEmployeeService iEmployeeService;
    /**
     * The Asso account employee repository.
     */
    @Autowired
    AssoAccountEmployeeRepository assoAccountEmployeeRepository;

    @Override
    public void performProcessor(Exchange exchange, String object) throws Exception {
        AssoAccountDto assoAccountDto = JsonHelper.fromJson(object, AssoAccountDto.class);
        String[] splitOrigin = assoAccountDto.getOrigin().split("-");
        Employee employee = iEmployeeService.findEmployeeByCode(splitOrigin[1]);
        if ("EMPLOYEE".equals(splitOrigin[0])) {
            assoAccountEmployeeRepository.save(AssoAccountEmployee.builder().accountCode(assoAccountDto.getCode()).employee(employee).build());
        }
    }
}