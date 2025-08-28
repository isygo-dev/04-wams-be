package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectRepository;
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
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = VacationRepository.class)
public class VacationService extends CrudService<Long, Vacation, VacationRepository> implements IVacationService {

}
