package eu.isygoit.controller;

import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.model.Template;
import eu.isygoit.service.impl.TemplateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/private/template")

public class TemplateRestController extends MappedCrudController<Long, Template, TemplateDto, TemplateDto, TemplateService> {
}
