package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.enums.IEnumContractType;
import eu.isygoit.model.Contract;
import eu.isygoit.model.PaymentSchedule;
import eu.isygoit.model.SalaryInformation;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.ContractRepository;
import eu.isygoit.repository.PaymentBonusScheduleRepository;
import eu.isygoit.repository.PaymentScheduleRepository;
import eu.isygoit.service.IPaymentScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * The type Payment schedule service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = PaymentScheduleRepository.class)
public class PaymentScheduleService extends CrudService<Long, PaymentSchedule, PaymentScheduleRepository> implements IPaymentScheduleService {

    /**
     * The Payment bonus schedule repository.
     */
    private final PaymentBonusScheduleRepository paymentBonusScheduleRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public PaymentScheduleService(PaymentBonusScheduleRepository paymentBonusScheduleRepository, ContractRepository contractRepository) {
        this.paymentBonusScheduleRepository = paymentBonusScheduleRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional
    public List<PaymentSchedule> calculatePaymentSchedule(Long contractId) {
        List<PaymentSchedule> paymentSchedules = new ArrayList<>();

        contractRepository.findById(contractId).ifPresent(contract -> {
            SalaryInformation salaryInformation = contract.getSalaryInformation();
            if (salaryInformation != null && salaryInformation.getPaymentSchedules().isEmpty()) {
                LocalDate startDate = contract.getStartDate();
                LocalDate endDate = contract.getEndDate();
                int frequency = salaryInformation.getFrequency();

                if (startDate != null && frequency > 0) {
                    // Handle CDI contract with no end date
                    if (endDate == null && contract.getContract() == IEnumContractType.Types.CDI) {
                        createPaymentSchedulesForCDIContract(startDate, frequency, salaryInformation, paymentSchedules);
                    }
                    // Handle contract with a specific end date
                    else if (endDate != null) {
                        createPaymentSchedulesForContractWithEndDate(startDate, endDate, frequency, salaryInformation, paymentSchedules);
                    }
                }

                // Save schedules after calculation
                salaryInformation.setPaymentSchedules(paymentSchedules);
                repository().saveAll(paymentSchedules);
            }
        });

        return paymentSchedules.isEmpty() ? Collections.emptyList() : paymentSchedules;
    }

    private void createPaymentSchedulesForCDIContract(LocalDate startDate, int frequency, SalaryInformation salaryInformation, List<PaymentSchedule> paymentSchedules) {
        double grossAmountPerPayment = salaryInformation.getGrossSalary() / frequency;
        double netAmountPerPayment = salaryInformation.getNetSalary() / frequency;

        // Use Stream.iterate() to generate the payment schedules
        Stream.iterate(startDate, currentDate -> currentDate.plusMonths(1))
                .limit(frequency)
                .forEach(currentDate -> {
                    LocalDate dueDate = currentDate.with(TemporalAdjusters.lastDayOfMonth());
                    PaymentSchedule paymentSchedule = PaymentSchedule.builder()
                            .dueDate(dueDate)
                            .paymentGrossAmount(grossAmountPerPayment)
                            .paymentNetAmount(netAmountPerPayment)
                            .build();
                    paymentSchedules.add(paymentSchedule);
                });
    }

    private void createPaymentSchedulesForContractWithEndDate(LocalDate startDate, LocalDate endDate, int frequency, SalaryInformation salaryInformation, List<PaymentSchedule> paymentSchedules) {
        long durationInDays = endDate.toEpochDay() - startDate.toEpochDay();
        double paymentAmount = salaryInformation.getGrossSalary() / frequency;
        long durationBetweenPayments = durationInDays / frequency;

        // Use Stream.iterate() to generate the payment schedules
        Stream.iterate(startDate, currentDate -> currentDate.plusDays(durationBetweenPayments))
                .limit(frequency)
                .forEach(dueDate -> {
                    PaymentSchedule paymentSchedule = PaymentSchedule.builder()
                            .dueDate(dueDate)
                            .paymentGrossAmount(paymentAmount)
                            .build();
                    paymentSchedules.add(paymentSchedule);
                });
    }
}