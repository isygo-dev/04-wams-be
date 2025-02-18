package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.DocComment;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.DocCommentsRepository;
import eu.isygoit.repository.DocumentRepository;
import eu.isygoit.service.IDocCommentsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = DocCommentsRepository.class)
public class DocCommentsService extends CrudService<Long, DocComment, DocCommentsRepository> implements IDocCommentsService {
}
