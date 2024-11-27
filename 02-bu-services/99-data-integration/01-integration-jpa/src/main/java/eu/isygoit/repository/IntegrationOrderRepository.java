package eu.isygoit.repository;

import eu.isygoit.model.IntegrationOrder;
import org.springframework.stereotype.Repository;


/**
 * The interface Integration order repository.
 */
@Repository
public interface IntegrationOrderRepository extends JpaPagingAndSortingSAASCodifiableRepository<IntegrationOrder, Long> {

}
