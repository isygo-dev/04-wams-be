package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumAbsenceType;
import eu.isygoit.enums.IEnumAbseneceStatus;
import lombok.Data;

/**
 * The type Vacation dto.
 */
@Data

public class VacationDto extends AbstractAuditableDto<Long> {

    private String startDate;
    private String endDate;
    private Double leaveTaken;
    private Double recoveryLeaveTaken;
    private IEnumAbsenceType.Types absence;
    private IEnumAbseneceStatus.Types status;
    private String comment;
}
