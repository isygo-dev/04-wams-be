package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.model.Workflow;

import java.util.List;

/**
 * The interface Workflow service.
 */
public interface IWorkflowService extends ICrudServiceMethod<Long, Workflow> {
    /**
     * Gets workflow not associated.
     *
     * @return the workflow not associated
     */
    List<String> getWorkflowNotAssociated();


}
