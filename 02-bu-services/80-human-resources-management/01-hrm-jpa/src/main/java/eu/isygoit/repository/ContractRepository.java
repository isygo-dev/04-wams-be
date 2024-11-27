package eu.isygoit.repository;

import eu.isygoit.model.Contract;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * The interface Contract repository.
 */
@Repository
public interface ContractRepository extends JpaPagingAndSortingSAASCodifiableRepository<Contract, Long> {

    /**
     * Update contract status.
     *
     * @param isLocked the is locked
     * @param id       the id
     */
    @Modifying
    @Query("update Contract c set c.isLocked = :isLocked  where c.id = :id")
    void updateContractStatus(@Param("isLocked") Boolean isLocked, @Param("id") Long id);
}
