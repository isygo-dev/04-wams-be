package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.SharedWithDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocumentMapper;
import eu.isygoit.mapper.SharedWithMapped;
import eu.isygoit.model.SharedWith;
import eu.isygoit.service.impl.DocumentService;
import eu.isygoit.service.impl.SharedWithService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = SharedWithMapped.class, minMapper = SharedWithMapped.class, service = SharedWithService.class)
@RequestMapping(value = "/api/v1/private/sharedWith")

public class SharedWithRestController extends MappedCrudController<Long, SharedWith , SharedWithDto,SharedWithDto,SharedWithService> {
}
