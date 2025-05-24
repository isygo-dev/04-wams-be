package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CrudServiceUtils;
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
@ServRepo(value = TimelineRepository.class)
public class TimelineService extends CrudServiceUtils<Long, Timeline, TimelineRepository> implements ITimeLineService {

    @Override
    public List<Timeline> getTimelineByDomainAndCode(String code, String domain) {
        return repository().findAllByDomainIgnoreCaseAndCodeIgnoreCase(domain, code);
    }
}
