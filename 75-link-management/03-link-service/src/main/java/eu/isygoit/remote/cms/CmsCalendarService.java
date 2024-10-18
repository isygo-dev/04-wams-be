package eu.isygoit.remote.cms;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.config.FeignConfig;
import eu.isygoit.dto.data.VCalendarDto;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * The interface Cms calendar service.
 */
@FeignClient(configuration = FeignConfig.class, name = "calendar-service", contextId = "cms-calendar", path = "/api/v1/private/calendar")
public interface CmsCalendarService extends IMappedCrudApi<Long, VCalendarDto, VCalendarDto> {

}
