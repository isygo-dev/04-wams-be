package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.WorkflowDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.WorkflowMapper;
import eu.isygoit.model.Workflow;
import eu.isygoit.service.impl.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Workflow controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/workflow")
@CtrlDef(handler = RpmExceptionHandler.class, mapper = WorkflowMapper.class, minMapper = WorkflowMapper.class, service = WorkflowService.class)
public class WorkflowController extends MappedCrudController<Long, Workflow, WorkflowDto, WorkflowDto, WorkflowService> {

    /**
     * Gets unassociated workflows.
     *
     * @param requestContext the request context
     * @return the unassociated workflows
     */
    @Operation(summary = "xxx Api",
            description = "xxx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/unassociated")
    ResponseEntity<List<String>> getUnassociatedWorkflows(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext) {
        try {
            return ResponseFactory.ResponseOk(crudService().getWorkflowNotAssociated());
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
