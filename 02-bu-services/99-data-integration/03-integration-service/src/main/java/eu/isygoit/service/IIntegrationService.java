package eu.isygoit.service;

import eu.isygoit.model.IntegrationOrder;

import java.util.List;

/**
 * The interface Integration service.
 *
 * @param <T> the type parameter
 */
public interface IIntegrationService<T> {

    /**
     * Create t.
     *
     * @param order  the order
     * @param object the object
     * @return the t
     */
    T create(IntegrationOrder order, T object);

    /**
     * Update t.
     *
     * @param order  the order
     * @param object the object
     * @return the t
     */
    T update(IntegrationOrder order, T object);

    /**
     * Delete boolean.
     *
     * @param order      the order
     * @param identifier the identifier
     * @return the boolean
     */
    boolean delete(IntegrationOrder order, T identifier);

    /**
     * Fetch list.
     *
     * @param order the order
     * @return the list
     */
    List<T> fetch(IntegrationOrder order);
}
