package eu.isygoit.controller;

import eu.isygoit.annotation.InjectExceptionHandler;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.ControllerExceptionHandler;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.ResumeGlobalStatDto;
import eu.isygoit.dto.data.ResumeStatDto;
import eu.isygoit.enums.IEnumResumeStatType;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.service.IResumeService;
import eu.isygoit.service.RequestContextService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * The type Resume statistics controller.
 */
@Slf4j
@Validated
@RestController
@InjectExceptionHandler(RpmExceptionHandler.class)
@RequestMapping(path = "/api/v1/private/resume/stat")
public class ResumeStatisticsController extends ControllerExceptionHandler {

    @Autowired
    private IResumeService resumeService;
    @Getter
    @Autowired
    private RequestContextService requestContextService;

    /**
     * Gets global statistics.
     
     * @param statType       the stat type
     * @return the global statistics
     */
    @Operation(summary = "Get Global Statistics Api",
            description = "Get Global Statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumeGlobalStatDto.class))})
    })
    @GetMapping(path = "/global")
    ResponseEntity<ResumeGlobalStatDto> getGlobalStatistics(@RequestParam(name = RestApiConstants.STAT_TYPE) IEnumResumeStatType.Types statType) {
        log.info("Get global statistics");
        try {
            return ResponseFactory.responseOk(resumeService.getGlobalStatistics(statType, getRequestContextService().getCurrentContext()));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Gets object statistics.
     
     * @param code           the code
     * @return the object statistics
     */
    @Operation(summary = "Get Object Statistics Api",
            description = "Get Object Statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumeStatDto.class))})
    })
    @GetMapping(path = "/object")
    ResponseEntity<ResumeStatDto> getObjectStatistics(
                                                      @RequestParam(name = RestApiConstants.CODE) String code) {
        log.info("Get object statistics with code: ", code);
        try {
            return ResponseFactory.responseOk(resumeService.getObjectStatistics(code, getRequestContextService().getCurrentContext()));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
