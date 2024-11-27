package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
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
public class JobOfferDto extends AbstractAuditableDto<Long> {

    private String domain;
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
