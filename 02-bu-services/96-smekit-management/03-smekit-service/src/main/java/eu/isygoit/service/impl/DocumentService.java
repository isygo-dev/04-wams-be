package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.AppNextCode;
import eu.isygoit.model.DocComment;
import eu.isygoit.model.Document;
import eu.isygoit.model.Template;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.DocCommentsRepository;
import eu.isygoit.repository.DocumentRepository;
import eu.isygoit.repository.TagRepository;
import eu.isygoit.service.IDocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = DocumentRepository.class)
public class DocumentService extends CodifiableService<Long, Document, DocumentRepository> implements IDocumentService {

    @Override
    public AppNextCode initCodeGenerator() {
        return AppNextCode.builder()
                .domain(DomainConstants.DEFAULT_DOMAIN_NAME)
                .entity(Document.class.getSimpleName())
                .attribute(SchemaColumnConstantName.C_CODE)
                .prefix("DOC")
                .valueLength(6L)
                .value(1L)
                .build();
    }
}
