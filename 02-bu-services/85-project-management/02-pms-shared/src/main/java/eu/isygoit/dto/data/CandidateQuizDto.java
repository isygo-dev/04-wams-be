package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * The type Candidate quiz dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class CandidateQuizDto extends AuditableDto<Long> {

    private String code;
    private Date startDate;
    private Date submitDate;
    private String quizCode;
    private Long score;
    private List<CandidateQuizAnswerDto> answers;
}
