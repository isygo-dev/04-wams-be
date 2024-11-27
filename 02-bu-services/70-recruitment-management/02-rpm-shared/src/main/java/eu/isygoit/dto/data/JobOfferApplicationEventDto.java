package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumJobAppEventType;
import eu.isygoit.enums.IEnumJobAppStatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Job offer application event dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class JobOfferApplicationEventDto extends AbstractAuditableDto<Long> {

    private IEnumJobAppEventType.Types type;
    @Builder.Default
    private IEnumJobAppStatusType.Types statusType = IEnumJobAppStatusType.Types.PLANNED;
    private String title;
    private String location;
    private String calendar;
    private String eventCode;
    private List<String> participants;
    private String comment;
}
