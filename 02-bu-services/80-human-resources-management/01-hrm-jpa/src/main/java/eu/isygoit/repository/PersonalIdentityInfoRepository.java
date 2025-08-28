package eu.isygoit.repository;

import eu.isygoit.model.PersonalIdentityInfo;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Personal identity info repository.
 */
@Repository
public interface PersonalIdentityInfoRepository extends JpaPagingAndSortingTenantAndCodeAssignableRepository<PersonalIdentityInfo, Long> {

}
