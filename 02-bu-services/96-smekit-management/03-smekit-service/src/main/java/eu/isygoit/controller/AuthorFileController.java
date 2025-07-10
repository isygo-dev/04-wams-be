package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.AuthorDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.AuthorMapper;
import eu.isygoit.model.Author;
import eu.isygoit.service.impl.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/author")
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = AuthorMapper.class, minMapper = AuthorMapper.class, service = AuthorService.class)
public class AuthorFileController extends MappedFileController<Long, Author, AuthorDto, AuthorDto, AuthorService> {
    private final AuthorService authorService;

    public AuthorFileController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/file/downloadPreview")
    public ResponseEntity<Resource> downloadAuthorFile(
            @RequestParam Long id,
            @RequestParam(required = false) Long version) {
        try {
            Resource file = authorService.downloadFile(id, version);
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
            log.error("Erreur de téléchargement du fichier de l’auteur", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
