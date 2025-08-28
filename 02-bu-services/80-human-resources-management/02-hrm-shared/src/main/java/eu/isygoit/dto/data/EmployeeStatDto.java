package eu.isygoit.dto.data;


import eu.isygoit.dto.extendable.IdAssignableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * The type Employee stat dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class EmployeeStatDto extends IdAssignableDto<Long> {
    private Integer contractCount;
    private LocalDate activeContractEndDate;
    private LocalDate activeContractAnniversaryDate;
    private LocalDate nextBonusDate;
}
