package eu.isygoit.repository;

import eu.isygoit.model.PaymentSchedule;
import eu.isygoit.repository.tenancy.JpaPagingAndSortingTenantAndCodeAssignableRepository;
import org.springframework.stereotype.Repository;


/**
 * The interface Payment schedule repository.
 */
@Repository
public interface PaymentScheduleRepository extends JpaPagingAndSortingRepository<PaymentSchedule, Long> {

}
