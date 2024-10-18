package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.IntegrationFlowFileDto;
import eu.isygoit.exception.handler.IntegrationExceptionHandler;
import eu.isygoit.mapper.IntegrationFlowFileMapper;
import eu.isygoit.model.IntegrationFlow;
import eu.isygoit.service.impl.IntegrationFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Integration flow file controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/integration/flow")
@CtrlDef(handler = IntegrationExceptionHandler.class, mapper = IntegrationFlowFileMapper.class, minMapper = IntegrationFlowFileMapper.class, service = IntegrationFlowService.class)
public class IntegrationFlowFileController extends MappedFileController<Long, IntegrationFlow, IntegrationFlowFileDto,
        IntegrationFlowFileDto, IntegrationFlowService> {

}
