package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.ServRepo;
import eu.isygoit.com.rest.service.CrudService;
import eu.isygoit.model.Contract;
import eu.isygoit.model.PaymentBonusSchedule;
import eu.isygoit.model.Prime;
import eu.isygoit.model.SalaryInformation;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.ContractRepository;
import eu.isygoit.repository.PaymentBonusScheduleRepository;
import eu.isygoit.service.IPaymentScheduleBonusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * The type Payment schedule bonus service.
 */
@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@ServRepo(value = PaymentBonusScheduleRepository.class)
public class PaymentScheduleBonusService extends CrudService<Long, PaymentBonusSchedule, PaymentBonusScheduleRepository> implements IPaymentScheduleBonusService {

    private final ContractRepository contractRepository;

    @Autowired
    public PaymentScheduleBonusService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public List<PaymentBonusSchedule> calculateBonusPaymentSchedule(Long contractId) {
        Optional<Contract> optionalContract = contractRepository.findById(contractId);
        List<PaymentBonusSchedule> bonusSchedules = new ArrayList<>();

        optionalContract.ifPresent(contract -> {
            SalaryInformation salaryInformation = contract.getSalaryInformation();
            if (salaryInformation != null && !CollectionUtils.isEmpty(salaryInformation.getPrimes())) {
                List<Prime> primes = salaryInformation.getPrimes();

                primes.stream().forEach(prime -> {
                    double montant = prime.getAnnualMinAmount() / prime.getAnnualFrequency();

                    if (CollectionUtils.isEmpty(prime.getBonusSchedules())) {
                        List<PaymentBonusSchedule> primeBonusSchedules = new ArrayList<>();
                        primeBonusSchedules.addAll(
                                IntStream.range(0, prime.getAnnualFrequency())
                                        .mapToObj(i -> PaymentBonusSchedule.builder()
                                                .paymentAmount(montant)
                                                .isSubmited(false)
                                                .build())
                                        .collect(Collectors.toList())
                        );
                        prime.setBonusSchedules(primeBonusSchedules);
                        repository().saveAll(primeBonusSchedules);
                        bonusSchedules.addAll(primeBonusSchedules);
                    } else {
                        bonusSchedules.addAll(prime.getBonusSchedules());
                    }
                });
            }
        });

        return bonusSchedules;
    }
}