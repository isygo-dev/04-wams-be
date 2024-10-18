package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.LeaveSummaryDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.LeaveSummaryMapper;
import eu.isygoit.model.LeaveSummary;
import eu.isygoit.service.impl.LeaveSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Leave summary controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = LeaveSummaryMapper.class, minMapper = LeaveSummaryMapper.class, service = LeaveSummaryService.class)
@RequestMapping(value = "/api/v1/private/leaveStatus")
public class LeaveSummaryController extends MappedCrudController<Long, LeaveSummary, LeaveSummaryDto, LeaveSummaryDto, LeaveSummaryService> {

}
