package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumJobAppEventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * The type Job offer application interview event request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class JobOfferApplicationInterviewEventRequestDto extends AbstractAuditableDto<Long> {

    private String domain;
    private String title;
    private IEnumJobAppEventType.Types type;
    private Date startDateTime;
    private Date endDateTime;
    private String location;
    private List<String> participants;
    private List<InterviewSkillsDto> skills;
    private String quizCode;
    private String comment;
    private CandidateDto candidate;
}
