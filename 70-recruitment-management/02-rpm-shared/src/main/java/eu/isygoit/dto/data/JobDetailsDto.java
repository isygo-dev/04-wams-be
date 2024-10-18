package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Job details dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JobDetailsDto extends AbstractAuditableDto<Long> {

    private String description;
    private Integer experienceMin;
    private Integer experienceMax;
    private List<String> responsibility;
    private JobInfoDto jobInfo;
    private ContractInfoDto contractInfo;

    private List<JobSkillsDto> hardSkills;

    private List<JobSkillsDto> softSkills;
}
