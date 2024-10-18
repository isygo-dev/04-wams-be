package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.model.Contract;

/**
 * The interface Contract service.
 */
public interface IContractService extends ICrudServiceMethod<Long, Contract>, IFileServiceMethods<Long, Contract> {
    /**
     * Update contract status contract.
     *
     * @param id       the id
     * @param isLocked the is locked
     * @return the contract
     */
    Contract updateContractStatus(Long id, Boolean isLocked);
}
