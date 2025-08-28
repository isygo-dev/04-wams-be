package eu.isygoit.repository;

import eu.isygoit.model.IntegrationFlow;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Integration flow repository.
 */
@Repository
public interface IntegrationFlowRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<IntegrationFlow, Long> {

}
