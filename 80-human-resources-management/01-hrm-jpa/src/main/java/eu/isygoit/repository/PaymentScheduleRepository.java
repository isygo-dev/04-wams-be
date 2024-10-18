package eu.isygoit.repository;

import eu.isygoit.model.PaymentSchedule;
import org.springframework.stereotype.Repository;


/**
 * The interface Payment schedule repository.
 */
@Repository
public interface PaymentScheduleRepository extends JpaPagingAndSortingRepository<PaymentSchedule, Long> {

}
