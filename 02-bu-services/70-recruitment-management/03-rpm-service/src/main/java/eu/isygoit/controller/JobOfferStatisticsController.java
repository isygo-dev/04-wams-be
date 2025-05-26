package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlHandler;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.ControllerExceptionHandler;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferGlobalStatDto;
import eu.isygoit.dto.data.JobOfferStatDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
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
@CtrlHandler(RpmExceptionHandler.class)
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
    @Operation(summary = "Get Global Statistics Api",
            description = "Get Global Statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/global")
    ResponseEntity<JobOfferGlobalStatDto> getGlobalStatistics(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext
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
    @Operation(summary = "Get Object Statistics Api",
            description = "Get Object Statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/object")
    ResponseEntity<JobOfferStatDto> getObjectStatistics(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
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
