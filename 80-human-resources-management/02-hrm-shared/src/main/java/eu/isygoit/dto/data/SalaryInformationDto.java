package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumSalaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * The type Salary information dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SalaryInformationDto extends AbstractAuditableDto<Long> {

    private Double grossSalary;
    private Double netSalary;
    private IEnumSalaryType.Types salaryType;
    private Integer frequency;
    private Set<PrimeDto> primes;
    private String currency;


}
