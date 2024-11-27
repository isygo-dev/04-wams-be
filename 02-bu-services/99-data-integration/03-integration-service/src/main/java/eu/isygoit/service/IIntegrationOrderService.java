package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.model.IntegrationOrder;


/**
 * The interface Integration order service.
 */
public interface IIntegrationOrderService extends ICrudServiceMethod<Long, IntegrationOrder>, IFileServiceMethods<Long, IntegrationOrder> {

}
