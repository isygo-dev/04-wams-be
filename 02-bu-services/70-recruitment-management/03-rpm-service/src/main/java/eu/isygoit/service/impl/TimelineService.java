package eu.isygoit.service.impl;

import eu.isygoit.annotation.InjectCodeGenKms;
import eu.isygoit.annotation.InjectCodeGen;
import eu.isygoit.annotation.InjectRepository;
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
@InjectCodeGen(value = NextCodeService.class)
@InjectCodeGenKms(value = KmsIncrementalKeyService.class)
@InjectRepository(value = TimelineRepository.class)
public class TimelineService extends CrudServiceUtils<Long, Timeline, TimelineRepository> implements ITimeLineService {

    @Override
    public List<Timeline> getTimelineByTenantAndCode(String code, String tenant) {
        return repository().findAllByTenantIgnoreCaseAndCodeIgnoreCase(tenant, code);
    }
}
