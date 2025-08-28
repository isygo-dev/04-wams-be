package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Job offer dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class JobOfferDto extends AuditableDto<Long> {

    private String tenant;
    private String code;
    private String title;
    private String industry;
    private String employerType;
    private String jobFunction;
    private String department;
    private String owner;
    private String customer;
    private JobDetailsDto details;
    private List<JobOfferLinkedFileDto> additionalFiles;

}
