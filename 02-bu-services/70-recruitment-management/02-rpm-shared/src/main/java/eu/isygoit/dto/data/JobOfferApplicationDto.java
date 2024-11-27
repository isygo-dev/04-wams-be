package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumBinaryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Job offer application dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class JobOfferApplicationDto extends AbstractAuditableDto<Long> {

    private String domain;
    private String code;
    private IdCodeDto resume;
    private IdCodeDto jobOffer;
    private List<JobOfferApplicationEventDto> jobApplicationEvents;
    @Builder.Default
    private IEnumBinaryStatus.Types binaryStatusType = IEnumBinaryStatus.Types.ENABLED;

}
