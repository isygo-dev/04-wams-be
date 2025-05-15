package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.TemplateMapper;
import eu.isygoit.model.Template;
import eu.isygoit.service.impl.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = TemplateMapper.class, minMapper = TemplateMapper.class, service = TemplateService.class)
@RequestMapping(value = "/api/v1/private/template")

public class TemplateController extends MappedCrudController<Long, Template, TemplateDto, TemplateDto, TemplateService> {
    private final TemplateService templateService;
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
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

        Template existingTemplate = templateService.findById(id);
        if (existingTemplate == null) {
            log.warn("⚠Template ID {} introuvable.", id);
            return ResponseEntity.notFound().build();
        }

        updatedTemplate.setId(id);
        Template savedTemplate = templateService.updateTemplate(updatedTemplate);

        log.info(" Template ID {} mis à jour avec succès.", id);
        return ResponseEntity.ok(savedTemplate);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Template>> getTemplatesByCategory(@PathVariable Long categoryId) {
        List<Template> templates = templateService.getTemplatesByCategory(categoryId);
        return ResponseEntity.ok(templates);
    }

}
