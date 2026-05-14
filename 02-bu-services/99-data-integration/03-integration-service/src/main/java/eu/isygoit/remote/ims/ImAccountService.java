package eu.isygoit.remote.ims;

import eu.isygoit.api.StatisticServiceApi;
import eu.isygoit.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * The interface Im account service.
 */
@FeignClient(configuration = FeignConfig.class, name = "identity-service", contextId = "ims-app-account", path = "/api/v1/private/account")
public interface ImAccountService extends StatisticServiceApi {

}
