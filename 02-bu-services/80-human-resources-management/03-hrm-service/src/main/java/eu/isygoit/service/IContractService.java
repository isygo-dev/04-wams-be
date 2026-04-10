package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceOperations;
import eu.isygoit.com.rest.service.media.IFileServiceOperations;
import eu.isygoit.model.Contract;

/**
 * The interface Contract service.
 */
public interface IContractService extends ICrudServiceOperations<Long, Contract>, IFileServiceOperations<Long, Contract> {
    /**
     * Update contract status contract.
     *
     * @param id       the id
     * @param isLocked the is locked
     * @return the contract
     */
    Contract updateContractStatus(Long id, Boolean isLocked);
}
