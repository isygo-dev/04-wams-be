package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.TemplateMapper;
import eu.isygoit.model.Template;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.impl.TemplateFavoriteService;
import eu.isygoit.service.impl.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = TemplateMapper.class, minMapper = TemplateMapper.class, service = TemplateService.class)
@RequestMapping(value = "/api/v1/private/template")

public class TemplateController extends MappedCrudController<Long, Template, TemplateDto, TemplateDto, TemplateService> {
    private final TemplateService templateService;
    private final TemplateFavoriteService favoriteService;
    private final TemplateMapper templateMapper;
    private final TemplateRepository templateRepository;


    public TemplateController(TemplateService templateService, TemplateFavoriteService favoriteService, TemplateMapper templateMapper, CategoryRepository categoryRepository, TemplateRepository templateRepository) {
        this.templateService = templateService;
        this.favoriteService = favoriteService;
        this.templateMapper = templateMapper;
        this.templateRepository = templateRepository;
    }
//    @PutMapping("update/{id}")
//    public ResponseEntity<Template> updateTemplate(@PathVariable Long id, @RequestBody Template updatedTemplate) {
//        Template existingTemplate = templateService.findById(id);
//        if (existingTemplate != null) {
//            updatedTemplate.setId(id);
//            Template savedTemplate = templateService.updateTemplate(updatedTemplate);
//            return ResponseEntity.ok(savedTemplate);
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PutMapping("update/{id}")
    public ResponseEntity<Template> updateTemplate(@PathVariable Long id, @RequestBody Template updatedTemplate) {
        log.info(" Mise à jour du template ID: {}", id);

        Optional<Template> optional = templateService.findById(id);
        if (!optional.isPresent()) {
            log.warn("Template ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }

        updatedTemplate.setId(id);
        Template savedTemplate = templateService.updateTemplate(updatedTemplate);

        log.info(" Template ID {} mis à jour avec succès.", id);
        return ResponseEntity.ok(savedTemplate);
    }

    //    @GetMapping("/category/{categoryId}")
//    public ResponseEntity<String> getTemplatesByCategory(@PathVariable Long categoryId) throws JsonProcessingException {
//        List<Template> templates = templateRepository.findByCategoryId(categoryId);
//        String json = new ObjectMapper().writeValueAsString(templates); // Manual JSON serialization
//        return ResponseEntity.ok()
//                .header("Content-Length", String.valueOf(json.getBytes().length))
//                .body(json);
//    }
//
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TemplateDto>> getTemplatesByCategory(@PathVariable Long categoryId) {
        try {
            log.info("Fetching templates for category ID: {}", categoryId);

            List<Template> templates = templateService.getTemplatesByCategory(categoryId);
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


//    @PostMapping("/pinned/{templateId}")
//    public ResponseEntity<Boolean> setPinned(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
//                                                       @PathVariable(name = "templateId") Long templateId) {
//            requestContext.getSenderUser();
//            return ResponseEntity.ok(Boolean.TRUE);
//    }
//
//    @GetMapping("/pinned")
//    public ResponseEntity<List<TemplateDto>> getPinned(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext) {
//        requestContext.getSenderUser();
//        return ResponseEntity.ok(new ArrayList<>());
//    }

    @PostMapping("/{templateId}/pin")
    public ResponseEntity<Boolean> togglePinStatus(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
            @PathVariable Long templateId) {

        String userIdentifier = requestContext.getSenderUser();
        log.info("Toggle pin status for template ID: {} by user: {}", templateId, userIdentifier);

        boolean isPinned = favoriteService.toggleFavorite(templateId, requestContext);
        return ResponseEntity.ok(isPinned);
    }

    @GetMapping("/pinned")
    public ResponseEntity<List<TemplateDto>> getPinnedTemplates(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext) {

        String userIdentifier = requestContext.getSenderUser();
        log.info("Fetching pinned templates for user: {}", userIdentifier);

        return ResponseEntity.ok(
                favoriteService.getUserFavorites(userIdentifier).stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{templateId}/pinned-status")
    public ResponseEntity<Boolean> checkPinStatus(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
            @PathVariable Long templateId) {

        String userIdentifier = Optional.ofNullable(requestContext)
                .map(RequestContextDto::getSenderUser)
                .orElse("default_user");
        log.info("Checking pin status for template ID: {} by user: {}", templateId, userIdentifier);

        return ResponseEntity.ok(
                favoriteService.isFavorite(userIdentifier, templateId)
        );
    }

    private TemplateDto convertToDto(Template template) {
        return templateMapper.entityToDto(template);
    }


    @GetMapping("/pinned/count")
    public ResponseEntity<Long> countMyPinnedTemplates(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext) {

        String userIdentifier = requestContext.getSenderUser();
        log.info("Comptage des templates épinglés pour l'utilisateur: {}", userIdentifier);

        return ResponseEntity.ok(favoriteService.countByUser(userIdentifier));
    }


}