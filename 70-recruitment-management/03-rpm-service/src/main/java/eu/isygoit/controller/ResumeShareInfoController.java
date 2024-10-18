package eu.isygoit.controller;

import eu.isygoit.com.rest.controller.impl.FakeCrudController;
import eu.isygoit.dto.data.ResumeShareInfoDto;
import eu.isygoit.model.ResumeShareInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Resume share info controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/resume/shareInfo")
public class ResumeShareInfoController extends FakeCrudController<Long, ResumeShareInfo, ResumeShareInfoDto, ResumeShareInfoDto> {
}
