package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.model.Author;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.AuthorRepository;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.service.IAutherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = AuthorRepository.class)
public class AuthorService extends CodifiableService<Long, Author, AuthorRepository> implements IAutherService {
}
