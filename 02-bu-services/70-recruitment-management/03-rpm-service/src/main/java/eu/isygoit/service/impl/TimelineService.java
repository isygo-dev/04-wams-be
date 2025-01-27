package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudServiceUtils;
import eu.isygoit.model.Timeline;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TimelineRepository;
import eu.isygoit.service.ITimeLineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = TimelineRepository.class)
public class TimelineService extends CrudServiceUtils<Timeline, TimelineRepository> implements ITimeLineService {

    @Override
    public List<Timeline> getTimelineByDomainAndCode(String code, String domain) {
        return repository().findAllByDomainIgnoreCaseAndCodeIgnoreCase(domain, code);
    }
}
