package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.JobOfferTemplateDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.JobOfferTemplateMapper;
import eu.isygoit.model.JobOfferTemplate;
import eu.isygoit.service.impl.JobOfferTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Job template controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/JobOfferTemplate")
@CtrlDef(handler = RpmExceptionHandler.class, mapper = JobOfferTemplateMapper.class, minMapper = JobOfferTemplateMapper.class, service = JobOfferTemplateService.class)
public class JobOfferTemplateController extends MappedCrudController<Long, JobOfferTemplate, JobOfferTemplateDto, JobOfferTemplateDto, JobOfferTemplateService> {

}
