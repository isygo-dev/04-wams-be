package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumQuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Quiz question dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class QuizQuestionDto extends AbstractAuditableDto<Long> {

    private String question;
    private IEnumQuestionType.Types type;
    private Integer order;
    private List<QuizOptionDto> options;
    private String textAnswer;
}
