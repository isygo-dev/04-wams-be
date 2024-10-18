package eu.isygoit.dto.data;


import eu.isygoit.dto.extendable.IdentifiableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The type Job offer stat dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class JobOfferStatDto extends IdentifiableDto<Long> {

    private Long completion;
    private Long applicationCount;
    private Long selectedProfilesCount; // ????
    private Long interviewedProfilesCount;
    private Long rejectedProfilesCount;// ????
}
