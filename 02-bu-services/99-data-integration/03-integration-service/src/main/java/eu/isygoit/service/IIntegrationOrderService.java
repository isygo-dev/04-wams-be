package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceOperations;
import eu.isygoit.com.rest.service.media.IFileServiceOperations;
import eu.isygoit.model.IntegrationOrder;


/**
 * The interface Integration order service.
 */
public interface IIntegrationOrderService extends ICrudServiceOperations<Long, IntegrationOrder>, IFileServiceOperations<Long, IntegrationOrder> {

}
