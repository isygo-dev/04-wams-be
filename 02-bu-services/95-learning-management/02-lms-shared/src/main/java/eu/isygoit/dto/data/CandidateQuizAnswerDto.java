package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The type Candidate quiz answer dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class CandidateQuizAnswerDto extends AuditableDto<Long> {

    private Long question;
    private Long option;
    private String answerText;
}
