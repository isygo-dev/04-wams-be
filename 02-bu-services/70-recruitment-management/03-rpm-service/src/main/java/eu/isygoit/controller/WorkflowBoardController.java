package eu.isygoit.controller;

import eu.isygoit.annotation.InjectMapperAndService;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.BpmEventRequestDto;
import eu.isygoit.dto.common.BpmEventResponseDto;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.JobOfferApplicationInterviewEventRequestDto;
import eu.isygoit.dto.data.WorkflowBoardDto;
import eu.isygoit.dto.data.WorkflowStateDto;
import eu.isygoit.dto.extendable.AccountModelDto;
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
@InjectMapperAndService(handler = RpmExceptionHandler.class, mapper = WorkflowBoardMapper.class, minMapper = WorkflowBoardMapper.class, service = WorkflowBoardService.class)
public class WorkflowBoardController extends MappedCrudController<Long, WorkflowBoard, WorkflowBoardDto, WorkflowBoardDto, WorkflowBoardService> {

    @Autowired
    private IWorkflowBoardService workflowBoardService;

    @Autowired
    private WorkflowStateMapper workflowStateMapper;

    /**
     * Gets states.
     
     * @param wbCode         the wb code
     * @return the states
     */
    @Operation(summary = "Get states by workflow board code",
            description = "This endpoint retrieves all states associated with a specific workflow board code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "States retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkflowStateDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No states found for the given code"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/states")
    ResponseEntity<List<WorkflowStateDto>> getStates(
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
     
     * @param wbCode         the wb code
     * @return the items
     */
    @Operation(summary = "Get items by workflow board code",
            description = "This endpoint retrieves all items associated with a specific workflow board code for a given tenant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Items retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IBoardItem.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No items found for the given code"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/items")
    ResponseEntity<List<IBoardItem>> getItems(
                                              @RequestParam(name = RestApiConstants.WB_CODE) String wbCode) {
        try {
            List<IBoardItem> list = workflowBoardService.getItems(getRequestContextService().getCurrentContext().getSenderTenant(), wbCode);
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
     
     * @param code           the code
     * @return the job application candidate
     */
    @Operation(summary = "Get candidate data for a job application",
            description = "This endpoint retrieves the candidate account data for a given application code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Candidate data retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountModelDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/candidate/{code}")
    ResponseEntity<AccountModelDto> getJobApplicationCandidate(
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
     
     * @return the item types
     */
    @Operation(summary = "Get item types",
            description = "This endpoint retrieves the list of item types that are status assignable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Item types retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No item types found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/itemTypes")
    ResponseEntity<List<String>> getItemTypes() {
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
     * @param getRequestContextService().getCurrentContext()  the request context
     * @param bpmEventRequest the bpm event request
     * @return the response entity
     */
    @Operation(summary = "Create BPM event",
            description = "This endpoint performs a BPM event based on the provided request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "BPM event performed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BpmEventResponseDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request provided"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping(path = "/event")
    ResponseEntity<BpmEventResponseDto> createEvent(
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
     
     * @param code           the code
     * @param id             the id
     * @return the event
     */
    @Operation(summary = "Get interview event",
            description = "This endpoint retrieves an interview event by its code and ID for the current tenant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Interview event retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobOfferApplicationInterviewEventRequestDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/board-event/{code}/{id}")
    ResponseEntity<JobOfferApplicationInterviewEventRequestDto> getEvent(
                                                                         @PathVariable(name = RestApiConstants.CODE) String code,
                                                                         @PathVariable(name = RestApiConstants.ID) Long id) {
        try {
            return ResponseFactory.responseOk(workflowBoardService.getInterviewEvent(getRequestContextService().getCurrentContext().getSenderTenant(), code, id));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Create event response entity.
     
     * @param eventType      the event type
     * @param code           the code
     * @param event          the event
     * @return the response entity
     */
    @Operation(summary = "Add interview event",
            description = "This endpoint adds a new interview event of a specific type for a given application code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Interview event added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid event data provided"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping(path = "/board-event/{eventType}/{code}")
    ResponseEntity<?> createEvent(
                                  @PathVariable(name = RestApiConstants.EVENT_TYPE) String eventType,
                                  @PathVariable(name = RestApiConstants.CODE) String code,
                                  @Valid @RequestBody JobOfferApplicationInterviewEventRequestDto event) {
        try {
            return ResponseFactory.responseOk(workflowBoardService.addInterviewEvent(getRequestContextService().getCurrentContext().getSenderTenant(), code, eventType, event));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Update event response entity.
     
     * @param eventType      the event type
     * @param code           the code
     * @param event          the event
     * @return the response entity
     */
    @Operation(summary = "Update interview event",
            description = "This endpoint updates an existing interview event of a specific type for a given application code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Interview event updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid event data provided"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PutMapping(path = "/board-event/{eventType}/{code}")
    ResponseEntity<?> updateEvent(
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
     
     * @param wfCode         the wf code
     * @return the available workflow emails
     */
    @Operation(summary = "Get available workflow emails",
            description = "This endpoint retrieves the list of emails associated with watchers for a specific workflow.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Watcher emails retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/watchers")
    ResponseEntity<List<String>> getAvailableWorkflowEmails(
                                                            @RequestParam(name = RestApiConstants.WF_CODE) String wfCode) {
        try {
            return ResponseFactory.responseOk(crudService().getBoardWatchersByWorkflow(wfCode, getRequestContextService().getCurrentContext().getSenderTenant()));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
