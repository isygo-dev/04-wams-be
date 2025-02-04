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
import java.util.List;
import java.util.Optional;


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
    final
    PaymentBonusScheduleRepository paymentBonusScheduleRepository;
    private final ContractRepository contractRepository;

    @Autowired
    public PaymentScheduleService(PaymentBonusScheduleRepository paymentBonusScheduleRepository, ContractRepository contractRepository) {
        this.paymentBonusScheduleRepository = paymentBonusScheduleRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    @Transactional
    public List<PaymentSchedule> calculatePaymentSchedule(Long contractId) {
        Optional<Contract> optionalContract = contractRepository.findById(contractId);
        List<PaymentSchedule> paymentSchedules = new ArrayList<>();
        if (optionalContract.isPresent()) {
            Contract contract = optionalContract.get();
            SalaryInformation salaryInformation = contract.getSalaryInformation();
            if (salaryInformation != null && salaryInformation.getPaymentSchedules().isEmpty()) {
                LocalDate startDate = contract.getStartDate();
                LocalDate endDate = contract.getEndDate();
                int frequency = salaryInformation.getFrequency();
                if (startDate != null && frequency > 0) {
                    if (endDate == null && contract.getContract() == IEnumContractType.Types.CDI) {
                        LocalDate currentDate = startDate;
                        for (int i = 0; i < frequency; i++) {
                            LocalDate dueDate = currentDate.with(TemporalAdjusters.lastDayOfMonth());
                            PaymentSchedule paymentSchedule = PaymentSchedule.builder()
                                    .dueDate(dueDate).paymentGrossAmount(salaryInformation.getGrossSalary() / frequency)
                                    .paymentNetAmount(salaryInformation.getNetSalary() / frequency)
                                    .build();
                            paymentSchedules.add(paymentSchedule);
                            currentDate = currentDate.plusMonths(1);
                        }
                    } else if (endDate != null && startDate != null) {
                        long durationInDays = endDate.toEpochDay() - startDate.toEpochDay();
                        double paymentAmount = salaryInformation.getGrossSalary() / frequency;
                        long durationBetweenPayments = durationInDays / frequency;
                        for (int i = 0; i < frequency; i++) {
                            LocalDate dueDate = startDate.plusDays(durationBetweenPayments * i);
                            PaymentSchedule paymentSchedule = PaymentSchedule.builder()
                                    .dueDate(dueDate).paymentGrossAmount(paymentAmount)
                                    .build();
                            paymentSchedules.add(paymentSchedule);
                        }
                    }
                }
                salaryInformation.setPaymentSchedules(paymentSchedules);
                repository().saveAll(paymentSchedules);
            }
            if (salaryInformation != null && !salaryInformation.getPaymentSchedules().isEmpty()) {
                return salaryInformation.getPaymentSchedules();
            }
        }
        return paymentSchedules;
    }
}