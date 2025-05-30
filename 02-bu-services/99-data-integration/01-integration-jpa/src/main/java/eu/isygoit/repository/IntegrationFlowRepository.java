package eu.isygoit.repository;

import eu.isygoit.model.IntegrationFlow;
import org.springframework.stereotype.Repository;


/**
 * The interface Integration flow repository.
 */
@Repository
public interface IntegrationFlowRepository extends JpaPagingAndSortingDomainAndCodeAssignableRepository<IntegrationFlow, Long> {

}
