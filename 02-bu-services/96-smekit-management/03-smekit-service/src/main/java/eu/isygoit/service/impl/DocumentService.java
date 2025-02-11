package eu.isygoit.service.impl;

import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.DocComment;
import eu.isygoit.model.Document;
import eu.isygoit.repository.DocCommentsRepository;
import eu.isygoit.repository.DocumentRepository;
import eu.isygoit.service.IDocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocumentService extends CrudService<Long, Document, DocumentRepository> implements IDocumentService {
}
