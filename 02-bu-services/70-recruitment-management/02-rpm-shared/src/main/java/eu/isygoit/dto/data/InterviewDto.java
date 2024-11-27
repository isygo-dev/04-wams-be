package eu.isygoit.dto.data;


import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Interview dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InterviewDto extends AbstractAuditableDto<Long> {

    private List<InterviewSkillsDto> skills;
    private String quizCode;
    private JobOfferApplicationEventDto jobApplicationEvent;

}
