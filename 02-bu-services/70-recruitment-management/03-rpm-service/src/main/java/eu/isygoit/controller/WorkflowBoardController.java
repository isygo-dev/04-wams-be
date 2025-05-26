package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.BpmEventRequestDto;
import eu.isygoit.dto.common.BpmEventResponseDto;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.JobOfferApplicationInterviewEventRequestDto;
import eu.isygoit.dto.data.WorkflowBoardDto;
import eu.isygoit.dto.data.WorkflowStateDto;
import eu.isygoit.dto.extendable.AccountModelDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.WorkflowBoardMapper;
import eu.isygoit.mapper.WorkflowStateMapper;
import eu.isygoit.model.IBoardItem;
import eu.isygoit.model.IStatusAssignable;
import eu.isygoit.model.WorkflowBoard;
import eu.isygoit.service.IWorkflowBoardService;
import eu.isygoit.service.impl.WorkflowBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Workflow board controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/workflow/board")
@CtrlDef(handler = RpmExceptionHandler.class, mapper = WorkflowBoardMapper.class, minMapper = WorkflowBoardMapper.class, service = WorkflowBoardService.class)
public class WorkflowBoardController extends MappedCrudController<Long, WorkflowBoard, WorkflowBoardDto, WorkflowBoardDto, WorkflowBoardService> {

    @Autowired
    private IWorkflowBoardService workflowBoardService;

    @Autowired
    private WorkflowStateMapper workflowStateMapper;

    /**
     * Gets states.
     *
     * @param requestContext the request context
     * @param wbCode         the wb code
     * @return the states
     */
    @Operation(summary = "getStates Api",
            description = "getStates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/states")
    ResponseEntity<List<WorkflowStateDto>> getStates(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                     @RequestParam(name = RestApiConstants.WB_CODE) String wbCode) {
        try {
            List<WorkflowStateDto> list = workflowStateMapper.listEntityToDto(workflowBoardService.getStates(wbCode))
                    .stream()
                    .peek(workflowStateDto -> workflowStateDto.setWbCode(wbCode))
                    .collect(Collectors.toUnmodifiableList());
            if (CollectionUtils.isEmpty(list)) {
                return ResponseFactory.responseNoContent();
            }
            return ResponseFactory.responseOk(list);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Gets items.
     *
     * @param requestContext the request context
     * @param wbCode         the wb code
     * @return the items
     */
    @Operation(summary = "getItems Api",
            description = "getItems")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })

    @GetMapping(path = "/items")
    ResponseEntity<List<IBoardItem>> getItems(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                              @RequestParam(name = RestApiConstants.WB_CODE) String wbCode) {
        try {
            List<IBoardItem> list = workflowBoardService.getItems(requestContext.getSenderDomain(), wbCode);
            if (CollectionUtils.isEmpty(list)) {
                return ResponseFactory.responseNoContent();
            }
            return ResponseFactory.responseOk(list);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Gets job application candidate.
     *
     * @param requestContext the request context
     * @param code           the code
     * @return the job application candidate
     */
    @Operation(summary = "getJobApplicationCandidate Api",
            description = "getJobApplicationCandidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/candidate/{code}")
    ResponseEntity<AccountModelDto> getJobApplicationCandidate(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                               @PathVariable(name = RestApiConstants.CODE) String code) {
        try {
            return ResponseFactory.responseOk(workflowBoardService.getCandidateData(code));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Gets item types.
     *
     * @param requestContext the request context
     * @return the item types
     */
    @Operation(summary = "getItemTypes Api",
            description = "getItemTypes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/itemTypes")
    ResponseEntity<List<String>> getItemTypes(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext) {
        try {
            List<String> list = this.exceptionHandler().getEntityMap().values().stream()
                    .filter(c -> IStatusAssignable.class.isAssignableFrom(c))
                    .map(aClass -> aClass.getName()).collect(Collectors.toUnmodifiableList());
            if (CollectionUtils.isEmpty(list)) {
                return ResponseFactory.responseNoContent();
            }
            return ResponseFactory.responseOk(list);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Create event response entity.
     *
     * @param requestContext  the request context
     * @param bpmEventRequest the bpm event request
     * @return the response entity
     */
    @Operation(summary = "createEvent Api",
            description = "createEvent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/event")
    ResponseEntity<BpmEventResponseDto> createEvent(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                    @Valid @RequestBody BpmEventRequestDto bpmEventRequest) {
        try {
            return ResponseFactory.responseOk(workflowBoardService.performEvent(bpmEventRequest));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Gets event.
     *
     * @param requestContext the request context
     * @param code           the code
     * @param id             the id
     * @return the event
     */
    @Operation(summary = "getEvent Api",
            description = "getEvent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/board-event/{code}/{id}")
    ResponseEntity<JobOfferApplicationInterviewEventRequestDto> getEvent(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                                         @PathVariable(name = RestApiConstants.CODE) String code,
                                                                         @PathVariable(name = RestApiConstants.ID) Long id) {
        try {
            return ResponseFactory.responseOk(workflowBoardService.getInterviewEvent(requestContext.getSenderDomain(), code, id));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Create event response entity.
     *
     * @param requestContext the request context
     * @param eventType      the event type
     * @param code           the code
     * @param event          the event
     * @return the response entity
     */
    @Operation(summary = "event Api",
            description = "event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/board-event/{eventType}/{code}")
    ResponseEntity<?> createEvent(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                  @PathVariable(name = RestApiConstants.EVENT_TYPE) String eventType,
                                  @PathVariable(name = RestApiConstants.CODE) String code,
                                  @Valid @RequestBody JobOfferApplicationInterviewEventRequestDto event) {
        try {
            return ResponseFactory.responseOk(workflowBoardService.addInterviewEvent(requestContext.getSenderDomain(), code, eventType, event));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Update event response entity.
     *
     * @param requestContext the request context
     * @param eventType      the event type
     * @param code           the code
     * @param event          the event
     * @return the response entity
     */
    @Operation(summary = "updateEvent Api",
            description = "updateEvent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PutMapping(path = "/board-event/{eventType}/{code}")
    ResponseEntity<?> updateEvent(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                  @PathVariable(name = RestApiConstants.EVENT_TYPE) String eventType,
                                  @PathVariable(name = RestApiConstants.CODE) String code,
                                  @Valid @RequestBody JobOfferApplicationInterviewEventRequestDto event) {
        try {
            return ResponseFactory.responseOk(workflowBoardService.editInterviewEvent(code, eventType, event));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Gets available workflow emails.
     *
     * @param requestContext the request context
     * @param wfCode         the wf code
     * @return the available workflow emails
     */
    @Operation(summary = "getAvailableWorkflowEmails Api",
            description = "getAvailableWorkflowEmails")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/watchers")
    ResponseEntity<List<String>> getAvailableWorkflowEmails(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                            @RequestParam(name = RestApiConstants.WF_CODE) String wfCode) {
        try {
            return ResponseFactory.responseOk(crudService().getBoardWatchersByWorkflow(wfCode, requestContext.getSenderDomain()));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
