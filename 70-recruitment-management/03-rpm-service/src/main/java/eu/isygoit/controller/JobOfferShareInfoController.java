package eu.isygoit.controller;

import eu.isygoit.com.rest.controller.impl.FakeCrudController;
import eu.isygoit.dto.data.JobOfferShareInfoDto;
import eu.isygoit.model.JobOfferShareInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type JobOffer share info controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/JobOffer/shareInfo")
public class JobOfferShareInfoController extends FakeCrudController<Long, JobOfferShareInfo, JobOfferShareInfoDto, JobOfferShareInfoDto> {
}
