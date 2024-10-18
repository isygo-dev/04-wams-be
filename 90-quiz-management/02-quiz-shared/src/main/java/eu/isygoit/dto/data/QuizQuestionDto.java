package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumQuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Quiz question dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizQuestionDto extends AbstractAuditableDto<Long> {

    private String question;
    private IEnumQuestionType.Types type;
    private Integer order;
    @Builder.Default
    private Long durationInSec = 60L;
    @Builder.Default
    private Long remainInSec = 60L;
    @Builder.Default
    private Boolean locked = Boolean.FALSE;
    private List<QuizOptionDto> options;
    private String textAnswer;
    private String imagePath;
    private String language;


    @Builder.Default
    private Double score = 0D;
}
