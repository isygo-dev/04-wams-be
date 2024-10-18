package eu.isygoit.repository;

import eu.isygoit.model.InsuranceIdentityInfo;
import org.springframework.stereotype.Repository;


/**
 * The interface Insurance identity info repository.
 */
@Repository
public interface InsuranceIdentityInfoRepository extends JpaPagingAndSortingRepository<InsuranceIdentityInfo, Long> {

}
