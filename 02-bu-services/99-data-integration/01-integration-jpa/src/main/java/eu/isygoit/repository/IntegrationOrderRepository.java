package eu.isygoit.repository;

import eu.isygoit.model.IntegrationOrder;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Integration order repository.
 */
@Repository
public interface IntegrationOrderRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<IntegrationOrder, Long> {

}
