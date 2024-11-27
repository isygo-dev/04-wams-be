package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumPrimeType;
import lombok.Data;

import java.util.List;

/**
 * The type Prime dto.
 */
@Data

public class PrimeDto extends AbstractAuditableDto<Long> {

    private IEnumPrimeType.Types primeType;
    private Double annualMaxAmount;
    private Double annualMinAmount;
    private Integer annualFrequency;
    private List<PaymentBonusScheduleDto> bonusSchedules;
}
