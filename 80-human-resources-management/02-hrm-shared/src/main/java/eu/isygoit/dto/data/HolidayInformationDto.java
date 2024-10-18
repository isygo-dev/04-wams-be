package eu.isygoit.dto.data;

import lombok.Data;


/**
 * The type Holiday information dto.
 */
@Data

public class HolidayInformationDto {

    private Double legalLeaveCount;
    private Integer feedingFrequency;
    private Double recoveryLeaveCount;
    private Integer recoveryFeedingFrequency;

}
