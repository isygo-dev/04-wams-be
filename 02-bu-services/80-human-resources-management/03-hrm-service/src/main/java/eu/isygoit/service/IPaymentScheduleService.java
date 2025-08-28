package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethods;
import eu.isygoit.model.PaymentSchedule;

import java.util.List;

/**
 * The interface Payment schedule service.
 */
public interface IPaymentScheduleService extends ICrudServiceMethods<Long, PaymentSchedule> {

    /**
     * Calculate payment schedule list.
     *
     * @param contractId the contract id
     * @return the list
     */
    List<PaymentSchedule> calculatePaymentSchedule(Long contractId);
}
