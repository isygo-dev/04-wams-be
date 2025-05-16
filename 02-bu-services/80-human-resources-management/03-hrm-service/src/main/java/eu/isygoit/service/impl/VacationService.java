package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.CrudService;
import eu.isygoit.model.Vacation;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.VacationRepository;
import eu.isygoit.service.IVacationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * The type Vacation service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = VacationRepository.class)
public class VacationService extends CrudService<Long, Vacation, VacationRepository> implements IVacationService {

}
