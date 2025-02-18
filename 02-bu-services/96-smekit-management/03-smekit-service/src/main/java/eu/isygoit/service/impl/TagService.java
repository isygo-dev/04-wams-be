package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.Tag;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.QuizRepository;
import eu.isygoit.repository.TagRepository;
import eu.isygoit.service.ITagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = TagRepository.class)
public class TagService extends CrudService<Long, Tag, TagRepository> implements ITagService {
}
