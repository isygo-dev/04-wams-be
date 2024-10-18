package eu.isygoit.repository;

import eu.isygoit.model.PaymentBonusSchedule;
import org.springframework.stereotype.Repository;


/**
 * The interface Payment bonus schedule repository.
 */
@Repository
public interface PaymentBonusScheduleRepository extends JpaPagingAndSortingRepository<PaymentBonusSchedule, Long> {

}
