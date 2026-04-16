package eu.isygoit.controller;

import eu.isygoit.annotation.InjectExceptionHandler;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.ControllerExceptionHandler;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.JobOfferGlobalStatDto;
import eu.isygoit.dto.data.JobOfferStatDto;
import eu.isygoit.enums.IEnumSharedStatType;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.service.IJobOfferService;
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


/**
 * The type Job offer statistics controller.
 */
@Slf4j
@Validated
@RestController
@InjectExceptionHandler(RpmExceptionHandler.class)
@RequestMapping(path = "/api/v1/private/JobOffer/stat")
public class JobOfferStatisticsController extends ControllerExceptionHandler {

    @Autowired
    private IJobOfferService jobOfferService;

    /**
     * Gets global statistics.
     *
     * @param requestContext the request context
     * @param statType       the stat type
     * @return the global statistics
     */
    @Operation(summary = "Get global job offer statistics",
            description = "This endpoint retrieves global statistics for job offers based on the specified statistic type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Global statistics retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobOfferGlobalStatDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/global")
    ResponseEntity<JobOfferGlobalStatDto> getGlobalStatistics(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) ContextRequestDto requestContext
            , @RequestParam(name = RestApiConstants.STAT_TYPE) IEnumSharedStatType.Types statType) {
        log.info("Get global statistics");
        try {
            return ResponseFactory.responseOk(jobOfferService.getGlobalStatistics(statType, requestContext));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Gets object statistics.
     *
     * @param requestContext the request context
     * @param code           the code
     * @return the object statistics
     */
    @Operation(summary = "Get object-specific job offer statistics",
            description = "This endpoint retrieves statistics for a specific job offer identified by its code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Object statistics retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobOfferStatDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/object")
    ResponseEntity<JobOfferStatDto> getObjectStatistics(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) ContextRequestDto requestContext,
                                                        @RequestParam(name = RestApiConstants.CODE) String code) {
        log.info("Get object statistics with code: ", code);
        try {
            return ResponseFactory.responseOk(jobOfferService.getObjectStatistics(code, requestContext));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
