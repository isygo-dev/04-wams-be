package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlHandler;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.impl.ControllerExceptionHandler;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.TimelineDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.TimelineMapper;
import eu.isygoit.service.impl.TimelineService;
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
 * The type Timeline controller.
 */
@Slf4j
@Validated
@RestController
@CtrlHandler(RpmExceptionHandler.class)
@RequestMapping(path = "/api/v1/private/timeline")
public class TimelineController extends ControllerExceptionHandler {
    /**
     * The Timeline service.
     */
    @Autowired
    TimelineService timelineService;
    /**
     * The Timeline mapper.
     */
    @Autowired
    TimelineMapper timelineMapper;

    /**
     * Find timeline response entity.
     *
     * @param requestContext the request context
     * @param code           the code
     * @param domain         the domain
     * @return the response entity
     */
    @Operation(summary = "findTimeline Api",
            description = "findTimeline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/{code}/{domain}")
    public ResponseEntity<List<TimelineDto>> findTimeline(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                          @PathVariable(name = RestApiConstants.CODE) String code,
                                                          @PathVariable(name = RestApiConstants.DOMAIN_NAME) String domain) {
        try {
            List<TimelineDto> timeline = timelineMapper.listEntityToDto(timelineService.getTimelineByDomainAndCode(code, domain));
            if (timeline.isEmpty()) {
                return ResponseFactory.responseNoContent();
            }
            return ResponseFactory.responseOk(timeline);
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }
}
