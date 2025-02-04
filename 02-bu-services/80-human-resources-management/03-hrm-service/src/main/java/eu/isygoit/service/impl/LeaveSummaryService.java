package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.model.LeaveSummary;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.LeaveSummaryRepository;
import eu.isygoit.service.ILeaveSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Leave summary service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = LeaveSummaryRepository.class)
public class LeaveSummaryService extends CrudService<Long, LeaveSummary, LeaveSummaryRepository> implements ILeaveSummaryService {

    private final AppProperties appProperties;

    /**
     * Instantiates a new Leave summary service.
     *
     * @param appProperties the app properties
     */
    public LeaveSummaryService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
}
