package eu.isygoit.repository;

import eu.isygoit.annotation.IgnoreRepository;
import eu.isygoit.model.nosql.IntegrationElement;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;


/**
 * The interface Integration element repository.
 */
@IgnoreRepository
public interface IntegrationElementRepository extends CassandraRepository<IntegrationElement, Long> {

    /**
     * Find by flow id list.
     *
     * @param flowId the flow id
     * @return the list
     */
    @AllowFiltering
    List<IntegrationElement> findByFlowId(Long flowId);
}
