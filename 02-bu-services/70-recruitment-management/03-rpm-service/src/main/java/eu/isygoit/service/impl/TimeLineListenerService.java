package eu.isygoit.service.impl;

import eu.isygoit.com.camel.repository.ICamelRepository;
import eu.isygoit.dto.data.JobOfferApplicationDto;
import eu.isygoit.dto.data.TimelineDto;
import eu.isygoit.enums.IEnumActionEvent;
import eu.isygoit.model.ITLEntity;
import eu.isygoit.model.JobOfferApplication;
import eu.isygoit.model.Resume;
import eu.isygoit.service.ITimeLineListenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TimeLineListenerService implements ITimeLineListenerService {
    @Autowired
    private ICamelRepository camelRepository;

    @Override
    public void performPostPersistTL(ITLEntity entity) {
        handleEntityEvent(entity, IEnumActionEvent.Types.PERSIST);
    }

    @Override
    public void performPostRemoveTL(ITLEntity entity) {
        handleEntityEvent(entity, IEnumActionEvent.Types.REMOVE);
    }

    @Override
    public void performPostUpdateTL(ITLEntity entity) {
        handleEntityEvent(entity, IEnumActionEvent.Types.UPDATE);
    }

    private void handleEntityEvent(ITLEntity entity, IEnumActionEvent.Types actionType) {
        if (entity instanceof Resume) {
            handleResumeEventAsync((Resume) entity, actionType);
        } else if (entity instanceof JobOfferApplication) {
            handleJobApplicationEventAsync((JobOfferApplication) entity, actionType);
        }
    }

    private void handleResumeEventAsync(Resume resume, IEnumActionEvent.Types actionType) {
        camelRepository.asyncSendBody(ICamelRepository.timeline_queue,
                TimelineDto.builder()
                        .domain(resume.getDomain())
                        .code(resume.getCode())
                        .action(actionType)
                        .object(resume.getClass().getCanonicalName())
                        .parentCode(resume.getCode())
                        .build()
        );
    }

    private void handleJobApplicationEventAsync(JobOfferApplication jobApplication, IEnumActionEvent.Types actionType) {
        camelRepository.asyncSendBody(ICamelRepository.timeline_queue,
                JobOfferApplicationDto.builder()
                        .domain(jobApplication.getDomain())
                        .code(jobApplication.getCode())
                        .build()
        );
    }
}

