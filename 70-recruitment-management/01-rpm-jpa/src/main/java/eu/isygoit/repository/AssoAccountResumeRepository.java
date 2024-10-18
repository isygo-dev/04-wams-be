package eu.isygoit.repository;

import eu.isygoit.model.AssoAccountResume;

import java.util.Optional;

/**
 * The interface Asso account resume repository.
 */
public interface AssoAccountResumeRepository extends JpaPagingAndSortingRepository<AssoAccountResume, Long> {

    /**
     * Find by account code ignore case optional.
     *
     * @param accountCode the account code
     * @return the optional
     */
    Optional<AssoAccountResume> findByAccountCodeIgnoreCase(String accountCode);

    /**
     * Find by resume code optional.
     *
     * @param resumeCode the resume code
     * @return the optional
     */
    Optional<AssoAccountResume> findByResume_Code(String resumeCode);
}
