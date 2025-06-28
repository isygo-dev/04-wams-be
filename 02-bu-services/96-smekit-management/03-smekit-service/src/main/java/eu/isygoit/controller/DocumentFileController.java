package eu.isygoit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.dto.data.DocumentHtmlResponseDto;
import eu.isygoit.enums.IEnumDocTempStatus;
import eu.isygoit.enums.IEnumPermissionLevel;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocumentMapper;
import eu.isygoit.model.Document;
import eu.isygoit.model.SharedWith;
import eu.isygoit.service.impl.DocumentService;
import eu.isygoit.service.impl.SharedWithService;
import eu.isygoit.service.impl.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/document")

@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocumentMapper.class, minMapper = DocumentMapper.class, service = DocumentService.class)

public class DocumentFileController extends MappedFileController<Long, Document, DocumentDto, DocumentDto, DocumentService> {


    private final DocumentService documentService;
    private final TemplateService templateService;
    private  final SharedWithService sharedWithService;

    public DocumentFileController(DocumentService documentService, TemplateService templateService, SharedWithService sharedWithService) {
        this.documentService = documentService;
        this.templateService = templateService;
        this.sharedWithService = sharedWithService;
    }

    @PutMapping("/{id}/save")
    public ResponseEntity<?> updateHtmlDocument(@PathVariable Long id, @RequestBody Map<String, String> request, Principal principal) {
        try {
            String updatedContent = request.get("html");
            if (updatedContent == null || updatedContent.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Contenu HTML manquant ou vide");
            }

            if (principal == null || principal.getName() == null) {
                log.warn("Utilisateur non authentifié");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentification requise");
            }

            String userCode = extractUserCode(principal.getName());

            Document existing = documentService.findById(id);
            if (existing == null) {
                return ResponseEntity.notFound().build();
            }

            log.info(" Tentative de modification du document {} par {}", id, userCode);

            if (existing.getCreatedBy() != null && extractUserCode(existing.getCreatedBy()).equalsIgnoreCase(userCode)) {
                log.info(" Modification autorisée : propriétaire du document");
            } else {
                Optional<SharedWith> shared = sharedWithService.getSharedPermission(id, userCode);

                if (shared.isEmpty()) {
                    log.warn("Accès refusé : document non partagé avec {}", userCode);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas accès à ce document");
                }

                if (IEnumPermissionLevel.PermissionLevel.READ.equals(shared.get().getPermission())) {
                    log.warn("Modification refusée : accès en lecture seule pour {}", userCode);
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'avez pas la permission de modifier ce document");
                }

                log.info("Modification autorisée : document partagé avec droit EDIT pour {}", userCode);
            }

            existing.setContent(updatedContent);
            existing.setEditionDate(LocalDateTime.now());
            existing.setTempType(IEnumDocTempStatus.Types.EDITING);

            existing.setDmsFileId(null);
            existing.setOriginalFileName(null);
            existing.setExtension(null);
            existing.setPath(null);

            Document updatedDoc = documentService.saveOrUpdate(existing);

            File tmp = File.createTempFile("updated_", ".docx");
            try {
                documentService.convertHtmlToDocx(updatedContent, tmp.getAbsolutePath());
                MultipartFile mp = documentService.convertFileToMultipart(tmp.getAbsolutePath());
                documentService.uploadDocumentFile(updatedDoc, mp);
            } finally {
                tmp.delete();
            }

            return ResponseEntity.ok(updatedDoc);

        } catch (Exception e) {
            log.error(" Erreur lors de la mise à jour du document", e);
            return ResponseEntity.internalServerError().body("Erreur lors de la mise à jour du document");
        }
    }

    @GetMapping("/{id}/html/")
    public ResponseEntity<String> getDocumentHtml(
            @PathVariable Long id,
            @RequestParam Long version) {
        try {
            Resource fileResource = documentService.downloadFile(id, version);

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
    @GetMapping("/{id}/html/details")
    public ResponseEntity<DocumentHtmlResponseDto> getDocumentHtmlWithMetadata(
            @PathVariable Long id,
            @RequestParam Long version) {
        try {
            ZipSecureFile.setMinInflateRatio(0.0001);

            Resource fileResource = documentService.downloadFile(id, version);

            if (fileResource == null || !fileResource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String html = templateService.convertToHtml(fileResource);
            Document document = documentService.findById(id);
            DocumentDto documentDto = mapper().entityToDto(document);

            return ResponseEntity.ok(new DocumentHtmlResponseDto(html, documentDto));

        } catch (IOException e) {
            log.error("Conversion error:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String extractUserCode(String raw) {
        if (raw == null) return "";
        String code = raw.contains("@") ? raw.substring(0, raw.indexOf("@")) : raw;
        return code.toUpperCase();
    }

}
