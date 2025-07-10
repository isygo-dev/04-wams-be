package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocumentMapper;
import eu.isygoit.model.Document;
import eu.isygoit.service.impl.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocumentMapper.class, minMapper = DocumentMapper.class, service = DocumentService.class)
@RequestMapping(value = "/api/v1/private/document")

public class DocumentController extends MappedCrudController<Long, Document, DocumentDto,DocumentDto, DocumentService> {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/from-template/{templateId}")
    public ResponseEntity<DocumentDto> createFromTemplate(
            @PathVariable Long templateId,
            @RequestBody DocumentDto request) {
        try {
            String content = request.getContent();
            String name = request.getName();

            log.info("Creating document from template ID: {} with name: {}, content length: {}",
                    templateId, name, content != null ? content.length() : 0);

            Document document = documentService.createFromTemplate(templateId, content, name);
            DocumentDto dto = mapper().entityToDto(document);

            log.info("Created DTO with ID: {}, content present: {}, isTemplateCopy: {}, originalDocumentId: {}",
                    dto.getId(),
                    dto.getContent() != null,
                    dto.getIsTemplateCopy(),
                    dto.getOriginalDocumentId());

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error creating document from template: {}", templateId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/by-user")
    public ResponseEntity<List<DocumentDto>> getDocumentsByUser(
            @RequestParam String createdBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        log.info("Fetching documents for user: {}", createdBy);

        var pageable = org.springframework.data.domain.PageRequest.of(page, size);
        var pageResult = documentService.findByCreatedBy(createdBy, pageable);
        var dtos = pageResult.map(mapper()::entityToDto).toList();

        return ResponseEntity.ok(dtos);
    }


}
