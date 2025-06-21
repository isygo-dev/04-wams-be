package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.enums.IEnumDocCommentsStaus;
import eu.isygoit.model.DocComment;
import eu.isygoit.model.Document;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.DocCommentsRepository;
import eu.isygoit.repository.DocumentRepository;
import eu.isygoit.service.IDocCommentsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = DocCommentsRepository.class)
public class DocCommentsService extends CrudService<Long, DocComment, DocCommentsRepository> implements IDocCommentsService {
    private final DocumentRepository documentRepository;

    public DocCommentsService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
    @Override
    public DocComment create(DocComment comment) {
        if (comment.getDocument() == null || comment.getDocument().getId() == null) {
            throw new IllegalArgumentException("Le document est obligatoire pour commenter.");
        }

        Long docId = comment.getDocument().getId();

        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new EntityNotFoundException("Document introuvable avec ID : " + docId));
        comment.setDocument(document);

        Integer start = comment.getStartOffset();
        Integer end = comment.getEndOffset();

        log.warn("Offsets reçus : start = {}, end = {}", start, end);

        if (start == null || end == null || start >= end) {
            throw new IllegalArgumentException("Offsets invalides.");
        }

        String htmlContent = document.getContent();
        if (htmlContent != null && !htmlContent.isBlank()) {
            String plainText = Jsoup.parse(htmlContent).text();
            log.warn("Texte brut [{} caractères] : {}", plainText.length(), plainText);

            if (start < plainText.length() && end <= plainText.length()) {
                String fragment = plainText.substring(start, end);
                comment.setTextCommented(fragment);
            } else {
                comment.setTextCommented("Fragment non valide");
            }
        } else {
            comment.setTextCommented("Aucun contenu HTML trouvé");
        }

        return super.create(comment);
    }


    public DocComment updateCommentType(Long commentId, IEnumDocCommentsStaus.Types newType) {
        DocComment comment = repository().findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Commentaire introuvable avec ID : " + commentId));

        comment.setType(newType);
        return repository().save(comment);
    }


    public List<DocComment> findByDocumentId(Long documentId) {
        return repository().findByDocumentId(documentId);
    }
}
