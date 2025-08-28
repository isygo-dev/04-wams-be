package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * The type Leave summary dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveSummaryDto extends AuditableDto<Long> {

    private Long id;
    private String codeEmployee;
    private String nameEmployee;
    private Double leaveCount;
    private Double leaveTakenCount;
    private Double remainingLeaveCount;
    private Double recoveryLeaveCount;
    private Double recoveryLeaveTaken;
    private Set<VacationDto> vacation;
}
