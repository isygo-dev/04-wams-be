package eu.isygoit.dto.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The type Job offer stat dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class JobOfferStatDto {

    private Long completion;
    private Long applicationCount;
    private Long selectedProfilesCount; // ????
    private Long interviewedProfilesCount;
    private Long rejectedProfilesCount;// ????
}
