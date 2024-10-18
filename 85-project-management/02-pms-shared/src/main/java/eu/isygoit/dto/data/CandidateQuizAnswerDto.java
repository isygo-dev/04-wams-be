package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The type Candidate quiz answer dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class CandidateQuizAnswerDto extends AbstractAuditableDto<Long> {

    private Long question;
    private Long option;
    private String answerText;
}
