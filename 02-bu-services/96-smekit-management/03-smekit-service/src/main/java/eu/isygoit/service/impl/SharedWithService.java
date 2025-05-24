package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CrudService;
import eu.isygoit.model.SharedWith;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.SharedWithRepository;
import eu.isygoit.service.ISharedWithService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = SharedWithRepository.class)
public class SharedWithService extends CrudService<Long, SharedWith, SharedWithRepository> implements ISharedWithService {


}
