package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferApplicationDto;
import eu.isygoit.dto.data.JobOfferDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.JobOfferApplicationMapper;
import eu.isygoit.mapper.JobOfferMapper;
import eu.isygoit.model.JobOfferApplication;
import eu.isygoit.service.IJobOfferService;
import eu.isygoit.service.impl.JobOfferApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Job offer application controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/JobOfferApplication")
@CtrlDef(handler = RpmExceptionHandler.class, mapper = JobOfferApplicationMapper.class, minMapper = JobOfferApplicationMapper.class, service = JobOfferApplicationService.class)
public class JobOfferApplicationController extends MappedCrudController<Long, JobOfferApplication, JobOfferApplicationDto, JobOfferApplicationDto, JobOfferApplicationService> {

    @Autowired
    private IJobOfferService jobOfferService;

    @Autowired
    private JobOfferMapper jobOfferMapper;


    /**
     * Gets job offers not assigned to resume.
     *
     * @param requestContext the request context
     * @param resumeCode     the resume code
     * @return the job offers not assigned to resume
     */
    @Operation(summary = "getJobOffersNotAssignedToResume Api",
            description = "getJobOffersNotAssignedToResume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/not-applied/{resumeCode}")
    public ResponseEntity<List<JobOfferDto>> getJobOffersNotAssignedToResume(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                                             @PathVariable(name = RestApiConstants.RESUME_CODE) String resumeCode) {
        try {
            List<JobOfferDto> jobOffers = jobOfferMapper.listEntityToDto(jobOfferService.findJobOffersNotAssignedToResume(resumeCode));
            if (jobOffers.isEmpty()) {
                return ResponseFactory.responseNoContent();
            }

            return ResponseFactory.responseOk(jobOffers);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
