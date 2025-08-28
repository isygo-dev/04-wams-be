package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethods;
import eu.isygoit.model.PaymentBonusSchedule;

import java.util.List;

/**
 * The interface Payment schedule bonus service.
 */
public interface IPaymentScheduleBonusService extends ICrudServiceMethods<Long, PaymentBonusSchedule> {
    /**
     * Calculate bonus payment schedule list.
     *
     * @param contractId the contract id
     * @return the list
     */
    List<PaymentBonusSchedule> calculateBonusPaymentSchedule(Long contractId);

}
