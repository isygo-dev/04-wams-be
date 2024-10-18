package eu.isygoit.remote.dms;

import eu.isygoit.api.LinkedFileApi;
import eu.isygoit.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * The interface Dms linked file service.
 */
@FeignClient(configuration = FeignConfig.class, name = "document-service", contextId = "dms-linked-file", path = "/api/v1/private/linked-files")
public interface DmsLinkedFileService extends LinkedFileApi {

}
