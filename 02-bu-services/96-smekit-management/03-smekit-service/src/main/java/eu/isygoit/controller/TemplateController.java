package eu.isygoit.controller;

import eu.isygoit.annotation.InjectMapperAndService;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.exception.ObjectNotFoundException;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.TemplateMapper;
import eu.isygoit.model.Template;
import eu.isygoit.service.impl.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@InjectMapperAndService(handler = SmeKitExceptionHandler.class, mapper = TemplateMapper.class, minMapper = TemplateMapper.class, service = TemplateService.class)
@RequestMapping(value = "/api/v1/private/template")
public class TemplateController extends MappedCrudController<Long, Template, TemplateDto, TemplateDto, TemplateService> {

    @Operation(summary = "Update template",
            description = "This endpoint updates an existing template identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Template updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Template.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Template not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PutMapping("update/{id}")
    public ResponseEntity<Template> updateTemplate(@PathVariable Long id, @RequestBody Template updatedTemplate) {
        log.info(" Mise à jour du template ID: {}", id);

        Optional<Template> optional = crudService().findById(id);
        if (!optional.isPresent()) {
            log.warn("Template ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }

        updatedTemplate.setId(id);
        Template savedTemplate = crudService().updateTemplate(updatedTemplate);

        log.info(" Template ID {} mis à jour avec succès.", id);
        return ResponseEntity.ok(savedTemplate);
    }

    @Operation(summary = "Get templates by category",
            description = "This endpoint retrieves all templates belonging to a specific category.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Templates retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemplateDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TemplateDto>> getTemplatesByCategory(@PathVariable Long categoryId) {
        try {
            log.info("Fetching templates for category ID: {}", categoryId);

            List<Template> templates = crudService().getTemplatesByCategory(categoryId);
            List<TemplateDto> templateDtos = templates.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            log.info("Successfully fetched {} templates for category ID: {}", templateDtos.size(), categoryId);
            return ResponseEntity.ok(templateDtos);

        } catch (Exception e) {
            log.error("Error fetching templates for category {}: {}", categoryId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Toggle pin status for a template",
            description = "This endpoint allows a user to pin or unpin a template.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Pin status toggled successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Template not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping("/{templateId}/pin")
    public ResponseEntity<Boolean> togglePinStatus(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) ContextRequestDto requestContext,
            @PathVariable Long templateId) {

        log.info("togglePinStatus");
        try {
            String userName = requestContext.getSenderUser();
            Optional<Template> optionalTemplate = crudService().findById(templateId);
            optionalTemplate.ifPresentOrElse(template -> {
                if (CollectionUtils.isEmpty(template.getFavorites())) {
                    template.setFavorites(Arrays.asList(userName));
                } else {
                    template.getFavorites().add(userName);
                }
                crudService().update(template);
            }, () -> {
                throw new ObjectNotFoundException(Template.class.getSimpleName() + " with id " + templateId);
            });

            return ResponseFactory.responseOk(Boolean.TRUE);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Operation(summary = "Get pinned templates",
            description = "This endpoint retrieves all templates pinned by the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Pinned templates retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemplateDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No pinned templates found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping("/pinned")
    public ResponseEntity<List<TemplateDto>> getPinnedTemplates(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) ContextRequestDto requestContext) {

        log.info("togglePinStatus");
        try {
            String userName = requestContext.getSenderUser();
            List<Template> pinned = crudService().findAllByFavoritesContaining(userName);
            if (CollectionUtils.isEmpty(pinned)) {
                return ResponseEntity.noContent().build();
            }

            return ResponseFactory.responseOk(mapper().listEntityToDto(pinned));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Operation(summary = "Check template pin status",
            description = "This endpoint checks if a specific template is pinned by the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Template is pinned",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404",
                    description = "Template is not pinned"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping("/{templateId}/pinned-status")
    public ResponseEntity<Boolean> checkPinStatus(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) ContextRequestDto requestContext,
            @PathVariable Long templateId) {

        log.info("checkPinStatus");
        try {
            String userName = requestContext.getSenderUser();
            log.info("Checking pin status for template ID: {} by user: {}", templateId, userName);

            Template template = crudService().findAllByIdAndFavoritesContaining(templateId, userName);
            if (template != null) {
                return ResponseFactory.responseOk(Boolean.TRUE);
            }

            return ResponseFactory.responseNotFound();
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    private TemplateDto convertToDto(Template template) {
        return mapper().entityToDto(template);
    }


    @Operation(summary = "Count pinned templates",
            description = "This endpoint retrieves the total number of templates pinned by the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Count retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping("/pinned/count")
    public ResponseEntity<Long> countMyPinnedTemplates(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) ContextRequestDto requestContext) {
        log.info("checkPinStatus");
        try {
            String userName = requestContext.getSenderUser();
            return ResponseEntity.ok(crudService().countTemplatesWithFavoriteUser(userName));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}