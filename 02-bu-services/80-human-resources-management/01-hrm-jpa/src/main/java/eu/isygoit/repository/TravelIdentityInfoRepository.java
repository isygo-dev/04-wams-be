package eu.isygoit.repository;

import eu.isygoit.model.TravelIdentityInfo;
import org.springframework.stereotype.Repository;


/**
 * The interface Travel identity info repository.
 */
@Repository
public interface TravelIdentityInfoRepository extends JpaPagingAndSortingRepository<TravelIdentityInfo, Long> {

}
