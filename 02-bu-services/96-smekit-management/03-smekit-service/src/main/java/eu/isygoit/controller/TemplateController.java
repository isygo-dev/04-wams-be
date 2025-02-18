package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.TemplateMapper;
import eu.isygoit.model.Template;
import eu.isygoit.service.impl.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = TemplateMapper.class, minMapper = TemplateMapper.class, service = TemplateService.class)
@RequestMapping(value = "/api/v1/private/template")

public class TemplateController extends MappedCrudController<Long, Template, TemplateDto, TemplateDto, TemplateService> {
}
