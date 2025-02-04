package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.VacationDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.VacationMapper;
import eu.isygoit.model.Vacation;
import eu.isygoit.service.impl.VacationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Vacation controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = VacationMapper.class, minMapper = VacationMapper.class, service = VacationService.class)
@RequestMapping(value = "/api/v1/private/vacation")
public class VacationController extends MappedCrudController<Long, Vacation, VacationDto, VacationDto, VacationService> {

}
