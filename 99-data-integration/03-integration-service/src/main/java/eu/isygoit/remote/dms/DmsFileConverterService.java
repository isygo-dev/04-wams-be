package eu.isygoit.remote.dms;

import eu.isygoit.api.FileConverterApi;
import eu.isygoit.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * The interface Dms file converter service.
 */
@FeignClient(configuration = FeignConfig.class, name = "document-service", contextId = "dms-file-converter", path = "/api/v1/private/file/convert")
public interface DmsFileConverterService extends FileConverterApi {

}
