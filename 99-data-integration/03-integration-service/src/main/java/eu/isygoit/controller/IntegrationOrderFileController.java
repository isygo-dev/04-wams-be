package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.IntegrationOrderFileDto;
import eu.isygoit.exception.handler.IntegrationExceptionHandler;
import eu.isygoit.mapper.IntegrationOrderFileMapper;
import eu.isygoit.model.IntegrationOrder;
import eu.isygoit.service.impl.IntegrationOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Integration order file controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/integration/order")
@CtrlDef(handler = IntegrationExceptionHandler.class, mapper = IntegrationOrderFileMapper.class, minMapper = IntegrationOrderFileMapper.class, service = IntegrationOrderService.class)
public class IntegrationOrderFileController extends MappedFileController<Long, IntegrationOrder, IntegrationOrderFileDto,
        IntegrationOrderFileDto, IntegrationOrderService> {

}
