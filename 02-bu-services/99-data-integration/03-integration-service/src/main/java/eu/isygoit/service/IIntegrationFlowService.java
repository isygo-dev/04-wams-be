package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.model.IntegrationFlow;
import eu.isygoit.model.nosql.IntegrationElement;

import java.util.List;

/**
 * The interface Integration flow service.
 */
public interface IIntegrationFlowService extends ICrudServiceMethod<Long, IntegrationFlow>, IFileServiceMethods<Long, IntegrationFlow> {

    /**
     * Find all integrated elements list.
     *
     * @param flowId the flow id
     * @return the list
     */
    List<IntegrationElement> findAllIntegratedElements(Long flowId);

    /**
     * Find integrated element by id integration element.
     *
     * @param elementId the element id
     * @return the integration element
     */
    IntegrationElement findIntegratedElementById(Long elementId);
}
