package eu.isygoit.repository;

import eu.isygoit.model.TravelIdentityInfo;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Travel identity info repository.
 */
@Repository
public interface TravelIdentityInfoRepository extends JpaPagingAndSortingCodeAssingnableRepository<TravelIdentityInfo, Long> {

}
