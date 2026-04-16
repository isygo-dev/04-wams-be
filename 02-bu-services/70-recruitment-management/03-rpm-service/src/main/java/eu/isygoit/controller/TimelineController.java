package eu.isygoit.controller;

import eu.isygoit.annotation.InjectExceptionHandler;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.impl.ControllerExceptionHandler;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.TimelineDto;
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
@InjectExceptionHandler(RpmExceptionHandler.class)
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
     * @param tenant         the tenant
     * @return the response entity
     */
    @Operation(summary = "Find timeline by code and tenant",
            description = "This endpoint retrieves the timeline events for a given code and tenant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Timeline retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TimelineDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No timeline found for the given code and tenant"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/{code}/{tenant}")
    public ResponseEntity<List<TimelineDto>> findTimeline(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) ContextRequestDto requestContext,
                                                          @PathVariable(name = RestApiConstants.CODE) String code,
                                                          @PathVariable(name = RestApiConstants.TENANT_NAME) String tenant) {
        try {
            List<TimelineDto> timeline = timelineMapper.listEntityToDto(timelineService.getTimelineByTenantAndCode(code, tenant));
            if (timeline.isEmpty()) {
                return ResponseFactory.responseNoContent();
            }
            return ResponseFactory.responseOk(timeline);
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }
}
