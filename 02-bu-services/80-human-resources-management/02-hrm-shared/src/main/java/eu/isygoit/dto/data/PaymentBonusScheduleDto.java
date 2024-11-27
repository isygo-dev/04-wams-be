package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * The type Payment bonus schedule dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PaymentBonusScheduleDto extends AbstractAuditableDto<Long> {


    private Double paymentAmount;
    private LocalDate submitDate;
    private LocalDate dueDate;
    private Boolean isSubmited;
}
