package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedImageController;
import eu.isygoit.dto.data.ResumeDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.ResumeMapper;
import eu.isygoit.model.Resume;
import eu.isygoit.service.impl.ResumeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Resume image controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/resume")
@CtrlDef(handler = RpmExceptionHandler.class, mapper = ResumeMapper.class, minMapper = ResumeMapper.class, service = ResumeService.class)
public class ResumeImageController extends MappedImageController<Long, Resume, ResumeDto, ResumeDto, ResumeService> {

}
