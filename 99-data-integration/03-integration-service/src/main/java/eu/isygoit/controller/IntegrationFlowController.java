package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.IntegrationElementDto;
import eu.isygoit.dto.data.IntegrationFlowFileDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.handler.IntegrationExceptionHandler;
import eu.isygoit.mapper.IntegrationElementMapper;
import eu.isygoit.mapper.IntegrationFlowFileMapper;
import eu.isygoit.model.IntegrationFlow;
import eu.isygoit.model.nosql.IntegrationElement;
import eu.isygoit.service.impl.IntegrationFlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Integration flow controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = IntegrationExceptionHandler.class, mapper = IntegrationFlowFileMapper.class, minMapper = IntegrationFlowFileMapper.class, service = IntegrationFlowService.class)
@RequestMapping(value = "/api/v1/private/integration/flow")
public class IntegrationFlowController extends MappedCrudController<Long, IntegrationFlow, IntegrationFlowFileDto,
        IntegrationFlowFileDto, IntegrationFlowService> {

    @Autowired
    private IntegrationElementMapper integrationElementMapper;

    /**
     * Find all integrated elements response entity.
     *
     * @param requestContext the request context
     * @param flowId         the flow id
     * @return the response entity
     */
    @Operation(summary = "Find all integrated elements",
            description = "Find all integrated elements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/elements")
    ResponseEntity<List<IntegrationElementDto>> findAllIntegratedElements(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                                          @RequestParam(name = RestApiConstants.FLOW_ID) Long flowId) {
        log.info("Find all integrated elements request received", IntegrationElement.class.getSimpleName());
        try {
            List<IntegrationElementDto> list = integrationElementMapper.listEntityToDto(this.crudService().findAllIntegratedElements(flowId));

            if (CollectionUtils.isEmpty(list)) {
                return ResponseFactory.ResponseNoContent();
            }

            return ResponseFactory.ResponseOk(list);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Find integrated element by id response entity.
     *
     * @param requestContext the request context
     * @param elementId      the element id
     * @return the response entity
     */
    @Operation(summary = "Find integrated element by id",
            description = "Find integrated element by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/element/id")
    ResponseEntity<IntegrationElementDto> findIntegratedElementById(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                                    @RequestParam(name = RestApiConstants.ID) Long elementId) {
        log.info("Find integrated element by id request received", IntegrationElement.class.getSimpleName());
        try {
            IntegrationElementDto element = integrationElementMapper.entityToDto(this.crudService().findIntegratedElementById(elementId));
            return ResponseFactory.ResponseOk(element);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
