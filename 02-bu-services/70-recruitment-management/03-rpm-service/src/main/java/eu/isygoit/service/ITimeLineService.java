package eu.isygoit.service;

import eu.isygoit.model.Timeline;

import java.util.List;


/**
 * The interface Time line service.
 */
public interface ITimeLineService {
    /**
     * Gets timeline by tenant and code.
     *
     * @param code   the code
     * @param tenant the tenant
     * @return the timeline by tenant and code
     */
    List<Timeline> getTimelineByTenantAndCode(String code, String tenant);
}
