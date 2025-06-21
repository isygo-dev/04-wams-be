package eu.isygoit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.TemplateMapper;
import eu.isygoit.model.Template;
import eu.isygoit.service.impl.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/template")
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = TemplateMapper.class, minMapper = TemplateMapper.class, service = TemplateService.class)
public class TemplateFileController extends MappedFileController<Long, Template, TemplateDto, TemplateDto, TemplateService> {
    private final TemplateService templateService;

    public TemplateFileController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/{id}/html/")
    public ResponseEntity<String> getTemplateHtml(
            @PathVariable Long id,
            @RequestParam Long version) {
        try {
            log.info("Tentative de conversion du fichier pour le template ID={} version={}", id, version);

            Resource fileResource = templateService.downloadFile(id, version);

            if (fileResource == null || !fileResource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String html = templateService.convertToHtml(fileResource);

            Map<String, String> response = new HashMap<>();
            response.put("html", html);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ObjectMapper().writeValueAsString(response));

        } catch (IOException e) {
            log.error("Conversion error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Failed to convert template\"}");
        }
    }


    @GetMapping("/file/downloadPreview")
    public ResponseEntity<Resource> downloadTemplateFile(
            @RequestParam Long id,
            @RequestParam(required = false) Long version
    ) {
        try {
            Resource file = templateService.downloadFile(id, version);


            if (file == null || !file.exists()) {
                return ResponseEntity.notFound().build();
            }

            String filename = file.getFilename();
            MediaType contentType;

            if (filename != null && filename.toLowerCase().endsWith(".pdf")) {
                contentType = MediaType.APPLICATION_PDF;
            } else if (filename != null && filename.toLowerCase().endsWith(".docx")) {
                contentType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            } else if (filename != null && filename.toLowerCase().endsWith(".doc")) {
                contentType = MediaType.parseMediaType("application/msword");
            } else {
                contentType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                    .body(file);

        } catch (Exception e) {
            log.error(" Erreur lors du téléchargement du fichier", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}/save")
    public ResponseEntity<?> updateHtmlTemplate(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String updatedContent = request.get("content");

            log.info("[Template Update] ID: {} - Contenu HTML REÇU :\n{}", id, updatedContent);

            if (updatedContent == null || updatedContent.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Missing or empty 'content' field in request body");
            }

            Template existing = templateService.findById(id);
            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            existing.setEditionDate(java.time.LocalDateTime.now());
            existing.setOriginalFileName(null);
            existing.setExtension(null);
            existing.setPath(null);

            Template updatedTemplate = templateService.saveOrUpdate(existing);

            File tmp = File.createTempFile("updated_template_", ".docx");
            try {
                log.info(" [Template Update] Conversion HTML ➜ DOCX...");

                templateService.convertHtmlToDocx(updatedContent, tmp.getAbsolutePath());
                MultipartFile mp = templateService.convertFileToMultipart(tmp.getAbsolutePath());

                log.info(" [Template Update] DOCX généré depuis HTML et prêt pour upload.");

                templateService.uploadFile(existing.getDomain(), existing.getId(), mp);
            } finally {
                tmp.delete();
            }

            log.info("[Template Update] Fichier template mis à jour avec succès : {}", updatedTemplate.getId());

            TemplateDto responseDto = mapper().entityToDto(updatedTemplate);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            log.error(" [Template Update] Erreur lors de la mise à jour du template", e);
            return ResponseEntity.internalServerError().body("Erreur lors de la mise à jour du template");
        }
    }

}