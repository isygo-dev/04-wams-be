package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.enums.IEnumPermissionLevel;
import eu.isygoit.model.Document;
import eu.isygoit.model.SharedWith;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.DocumentRepository;
import eu.isygoit.repository.SharedWithRepository;
import eu.isygoit.service.ISharedWithService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = SharedWithRepository.class)
public class SharedWithService extends CrudService <Long, SharedWith, SharedWithRepository> implements ISharedWithService {

    private final SharedWithRepository sharedWithRepository;
    private final DocumentRepository documentRepository;

    public SharedWithService(SharedWithRepository sharedWithRepository, DocumentRepository documentRepository) {
        this.sharedWithRepository = sharedWithRepository;
        this.documentRepository = documentRepository;
    }

    @Transactional
    public void shareDocument(Long documentId, String userCode, IEnumPermissionLevel.PermissionLevel permission) {
        log.info("Tentative de partage du document {} avec l'utilisateur {} en mode {}", documentId, userCode, permission);

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document introuvable"));

        boolean alreadyShared = sharedWithRepository.existsByDocumentAndUser(document, userCode);
        if (alreadyShared) {
            log.warn("Document {} déjà partagé avec l'utilisateur {}", documentId, userCode);
            throw new IllegalStateException("Document déjà partagé avec cet utilisateur");
        }

        SharedWith sharedWith = SharedWith.builder()
                .document(document)
                .user(userCode)
                .permission(permission)
                .build();

        sharedWithRepository.save(sharedWith);

        log.info("Partage enregistré avec succès : {} (user={}, documentId={}, permission={})", sharedWith.getId(), userCode, documentId, permission);
    }
    public Optional<SharedWith> getSharedPermission(Long documentId, String userCode) {
        Document doc = documentRepository.findById(documentId).orElse(null);
        if (doc == null) {
            return Optional.empty();
        }
        return sharedWithRepository.findByDocumentAndUser(doc, userCode.toUpperCase());
    }



    public boolean hasEditPermission(Long documentId, String userCode) {
        return getSharedPermission(documentId, userCode)
                .map(s -> IEnumPermissionLevel.PermissionLevel.EDIT.equals(s.getPermission()))
                .orElse(false);
    }

    public boolean hasReadPermission(Long documentId, String userCode) {
        return getSharedPermission(documentId, userCode).isPresent();
    }



    @Transactional(readOnly = true)
    public List<SharedWith> getDocumentsSharedWithUser(String userCode) {
        return sharedWithRepository.findByUser(userCode);
    }
    @Transactional(readOnly = true)
    public List<SharedWith> getDocumentShares(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new IllegalArgumentException("Document non trouvé"));

        return sharedWithRepository.findByDocument(document);
    }
}
