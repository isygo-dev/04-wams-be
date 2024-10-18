package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * The type Payment schedule dto.
 */
@Data

public class PaymentScheduleDto extends AbstractAuditableDto<Long> {

    private Boolean isSubmited;
    private Date submitDate;
    private LocalDate dueDate;
    private Double paymentGrossAmount;
    private Double paymentNetAmount;


}
