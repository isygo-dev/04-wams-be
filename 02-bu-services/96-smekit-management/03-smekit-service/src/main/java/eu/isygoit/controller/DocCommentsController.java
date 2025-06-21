package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.DocCommentDto;
import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.enums.IEnumDocCommentsStaus;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocCommentsMapper;
import eu.isygoit.model.DocComment;
import eu.isygoit.model.Document;
import eu.isygoit.service.impl.DocCommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocCommentsMapper.class, minMapper = DocCommentsMapper.class, service = DocCommentsService.class)
@RequestMapping(value = "/api/v1/private/docComments")

public class DocCommentsController extends MappedCrudController<Long, DocComment, DocCommentDto,DocCommentDto, DocCommentsService> {

    private final DocCommentsService service;

    public DocCommentsController(DocCommentsService service) {
        this.service = service;
    }
    @PostMapping("/custom")
    public DocCommentDto createComment(@RequestBody DocCommentDto dto) {
        log.warn("RECU: {}", dto);

        log.warn("DEBUG POST comment - startOffset={}, endOffset={}, documentId={}, type={}",
                dto.getStartOffset(), dto.getEndOffset(),
                dto.getDocument() != null ? dto.getDocument().getId() : null,
                dto.getType());

        if (dto.getDocument() == null || dto.getDocument().getId() == null) {
            throw new IllegalArgumentException("Document ID obligatoire.");
        }

        if (dto.getStartOffset() == null || dto.getEndOffset() == null || dto.getStartOffset() >= dto.getEndOffset()) {
            throw new IllegalArgumentException("Offsets invalides.");
        }
        DocComment comment = new DocComment();
        comment.setText(dto.getText());
        comment.setUser(dto.getUser());
        comment.setStartOffset(dto.getStartOffset());
        comment.setEndOffset(dto.getEndOffset());
        comment.setTextCommented(dto.getTextCommented());
        comment.setType(dto.getType());

        Document doc = new Document();
        doc.setId(dto.getDocument().getId());
        comment.setDocument(doc);

        DocComment saved = service.create(comment);
        return mapper().entityToDto(saved);
    }

    @GetMapping("/by-document/{documentId}")
    public List<DocCommentDto> getByDocumentId(@PathVariable Long documentId) {
        return service.findByDocumentId(documentId).stream().map(entity -> {
            DocCommentDto dto = new DocCommentDto();
            dto.setId(entity.getId());
            dto.setCreateDate(entity.getCreateDate());
            dto.setCreatedBy(entity.getCreatedBy());
            dto.setUpdateDate(entity.getUpdateDate());
            dto.setUpdatedBy(entity.getUpdatedBy());

            dto.setText(entity.getText());
            dto.setUser(entity.getUser());
            dto.setStartOffset(entity.getStartOffset());
            dto.setEndOffset(entity.getEndOffset());
            dto.setTextCommented(entity.getTextCommented());
            dto.setType(entity.getType());

            if (entity.getDocument() != null) {
                dto.setDocument(new DocumentDto());
                dto.getDocument().setId(entity.getDocument().getId());
            }

            return dto;
        }).collect(Collectors.toList());
    }
    @PatchMapping("/{id}/type")
    public ResponseEntity<DocComment> updateType(
            @PathVariable Long id,
            @RequestBody UpdateTypeRequest request
    ) {
        return ResponseEntity.ok(service.updateCommentType(id, request.getNewType()));
    }

    public static class UpdateTypeRequest {
        private IEnumDocCommentsStaus.Types newType;

        public IEnumDocCommentsStaus.Types getNewType() {
            return newType;
        }

        public void setNewType(IEnumDocCommentsStaus.Types newType) {
            this.newType = newType;
        }
    }

}
