package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Resume details dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDetailsDto extends AbstractAuditableDto<Long> {

    private List<ResumeProfExperienceDto> profExperiences;
    private List<ResumeEducationDto> educations;
    private List<ResumeSkillsDto> skills;
    private List<ResumeCertificationDto> certifications;
    private List<ResumeLanguageDto> languages;
}
