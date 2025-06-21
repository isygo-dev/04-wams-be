package eu.isygoit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.dto.data.DocumentHtmlResponseDto;
import eu.isygoit.enums.IEnumDocTempStatus;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocumentMapper;
import eu.isygoit.model.Document;
import eu.isygoit.service.impl.DocumentService;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/document")

@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocumentMapper.class, minMapper = DocumentMapper.class, service = DocumentService.class)

public class DocumentFileController extends MappedFileController<Long, Document, DocumentDto, DocumentDto, DocumentService> {


    private final DocumentService documentService;
    private final TemplateService templateService;


    public DocumentFileController(DocumentService documentService, TemplateService templateService) {
        this.documentService = documentService;
        this.templateService = templateService;
    }

    @PutMapping("/{id}/save")
    public ResponseEntity<?> updateHtmlDocument(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String updatedContent = request.get("html");
            log.info("🔍 Contenu HTML reçu pour MAJ : {}", updatedContent);
            if (updatedContent == null || updatedContent.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Missing or empty 'html' content");
            }

            Document existing = documentService.findById(id);
            if (existing == null) {
                return ResponseEntity.notFound().build();
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
            log.error("Erreur lors de la mise à jour du document", e);
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
}
