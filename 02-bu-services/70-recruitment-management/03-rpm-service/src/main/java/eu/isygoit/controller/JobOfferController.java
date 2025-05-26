package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferDto;
import eu.isygoit.dto.data.JobOfferShareInfoDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.dto.request.ShareJobRequestDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.JobOfferMapper;
import eu.isygoit.mapper.JobOfferShareInfoMapper;
import eu.isygoit.model.JobOffer;
import eu.isygoit.service.impl.JobOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Job offer controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/JobOffer")
@CtrlDef(handler = RpmExceptionHandler.class, mapper = JobOfferMapper.class, minMapper = JobOfferMapper.class, service = JobOfferService.class)
public class JobOfferController extends MappedCrudController<Long, JobOffer, JobOfferDto, JobOfferDto, JobOfferService> {

    @Autowired
    private JobOfferShareInfoMapper jobShareInfoMapper;

    /**
     * Share response entity.
     *
     * @param requestContext     the request context
     * @param id                 the id
     * @param shareJobRequestDto the share job request dto
     * @return the response entity
     */
    @Operation(summary = "share Api",
            description = "share")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/share/{id}")
    public ResponseEntity<List<JobOfferShareInfoDto>> share(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                            @PathVariable(name = RestApiConstants.ID) Long id,
                                                            @Valid @RequestBody ShareJobRequestDto shareJobRequestDto) {
        log.info("share job ");
        try {
            return ResponseFactory.responseOk(jobShareInfoMapper.listEntityToDto(crudService().shareJob(id, shareJobRequestDto.getJobOwner(), shareJobRequestDto.getAccountsCode())));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
