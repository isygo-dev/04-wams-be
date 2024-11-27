package eu.isygoit.service;

import eu.isygoit.model.Timeline;

import java.util.List;


/**
 * The interface Time line service.
 */
public interface ITimeLineService {
    /**
     * Gets timeline by domain and code.
     *
     * @param code   the code
     * @param domain the domain
     * @return the timeline by domain and code
     */
    List<Timeline> getTimelineByDomainAndCode(String code, String domain);
}
