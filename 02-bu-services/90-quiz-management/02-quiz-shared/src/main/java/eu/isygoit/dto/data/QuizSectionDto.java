package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * The type Quiz section dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizSectionDto extends AbstractAuditableDto<Long> {

    private String name;
    private String description;
    private Integer order;
    private List<QuizQuestionDto> questions;
    @Builder.Default
    private Double score = 0D;

    /**
     * Gets score.
     *
     * @return the score
     */
    public Double getScore() {
        if (this.score == 0L && !CollectionUtils.isEmpty(questions)) {
            this.score = questions.stream()
                    .map(quizQuestion -> quizQuestion.getScore())
                    .reduce(0D, Double::sum);
        }

        return this.score;
    }
}
