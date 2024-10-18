package eu.isygoit.repository;

import eu.isygoit.model.PersonalIdentityInfo;
import org.springframework.stereotype.Repository;


/**
 * The interface Personal identity info repository.
 */
@Repository
public interface PersonalIdentityInfoRepository extends JpaPagingAndSortingRepository<PersonalIdentityInfo, Long> {

}
