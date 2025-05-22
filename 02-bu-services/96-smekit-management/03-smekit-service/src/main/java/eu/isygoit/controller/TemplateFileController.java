package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.TemplateDto;

import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.TemplateMapper;
import eu.isygoit.model.Template;
import eu.isygoit.service.impl.TemplateService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/templatefile")
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = TemplateMapper.class, minMapper = TemplateMapper.class, service = TemplateService.class)
public class TemplateFileController extends MappedFileController<Long, Template, TemplateDto, TemplateDto, TemplateService> {

//    @PutMapping(path = {"/file"}, consumes = {"multipart/form-data"})
//    public ResponseEntity<TemplateDto> updateWithFile(
//            @RequestAttribute(value = "user-context", required = false) RequestContextDto requestContext,
//            @RequestParam(name = "id") Long id,
//            @ModelAttribute("fileUpload") TemplateDto dto) {
//
//        Optional<Template> templateOptional = Optional.ofNullable(crudService().findById(id));
//        if (!templateOptional.isPresent()) {
//            throw new RuntimeException("Template not found with id: " + id);
//        }
//        Template existingTemplate = templateOptional.get();
//
//        // Solution alternative 2: Avec orElseThrow (si le problème persiste)
//        // Template existingTemplate = ((TemplateService)crudService()).findByIdOrThrow(id);
//
//        // Conservation du fichier existant si aucun nouveau n'est fourni
//        if (dto.getFile() == null && existingTemplate.getOriginalFileName() != null) {
//            dto.setOriginalFileName(existingTemplate.getOriginalFileName());
//            // Si nécessaire, copiez aussi le contenu du fichier
//            // dto.setFileContent(existingTemplate.getFileContent());
//        }
//
//        return super.updateWithFile(requestContext, id, dto);
//    }
}