package eu.isygoit.remote.cms;

import eu.isygoit.api.CalendarEventControllerAPI;
import eu.isygoit.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * The interface Cms calendar event service.
 */
@FeignClient(configuration = FeignConfig.class, name = "calendar-service", contextId = "cms-calendar-event", path = "/api/v1/private/calendar/event")
public interface CmsCalendarEventService extends CalendarEventControllerAPI {

}
