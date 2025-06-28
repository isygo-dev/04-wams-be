package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.SharedWithDto;
import eu.isygoit.enums.IEnumPermissionLevel;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.SharedWithMapped;
import eu.isygoit.model.SharedWith;
import eu.isygoit.repository.SharedWithRepository;
import eu.isygoit.service.impl.SharedWithService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = SharedWithMapped.class, minMapper = SharedWithMapped.class, service = SharedWithService.class)
@RequestMapping(value = "/api/v1/private/sharedWith")

public class SharedWithController extends MappedCrudController<Long, SharedWith , SharedWithDto,SharedWithDto,SharedWithService> {

    private final SharedWithService documentShareService;

    public SharedWithController(SharedWithService documentShareService, SharedWithRepository sharedWithRepository) {
        this.documentShareService = documentShareService;
    }

    @PostMapping("/{id}/share")
    public void shareDocument(@PathVariable("id") Long documentId,
                              @RequestBody ShareRequest request) {
        log.info("API Reçue : Partage du document {} à l'utilisateur {} avec permission {}", documentId, request.user(), request.permission());
        documentShareService.shareDocument(documentId, request.user(), request.permission());
    }


    @GetMapping("/{id}/shares")
    public List<SharedWith> getShares(@PathVariable("id") Long documentId) {
        return documentShareService.getDocumentShares(documentId);
    }
    @GetMapping("/user/{userCode}/documents")
    public List<SharedWith> getDocumentsSharedWithUser(@PathVariable String userCode) {
        log.info("Récupération des documents partagés avec l'utilisateur : {}", userCode);
        return documentShareService.getDocumentsSharedWithUser(userCode);
    }


    public record ShareRequest(String user, IEnumPermissionLevel.PermissionLevel permission) {}
}

