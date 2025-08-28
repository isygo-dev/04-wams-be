package eu.isygoit.repository;

import eu.isygoit.model.InsuranceIdentityInfo;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Insurance identity info repository.
 */
@Repository
public interface InsuranceIdentityInfoRepository extends JpaPagingAndSortingCodeAssingnableRepository<InsuranceIdentityInfo, Long> {

}
