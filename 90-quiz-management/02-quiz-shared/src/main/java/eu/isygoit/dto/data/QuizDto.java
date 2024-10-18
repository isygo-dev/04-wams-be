package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumSkillLevelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * The type Quiz dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizDto extends AbstractAuditableDto<Long> {

    private String domain;
    private String code;
    private String name;
    private String description;
    private String category;
    @Builder.Default
    private IEnumSkillLevelType.Types level = IEnumSkillLevelType.Types.BEGINNER;
    private List<QuizSectionDto> sections;
    private List<String> tags;
    private Date startDate;
    private Date submitDate;
    @Builder.Default
    private Double score = 0D;
    @Builder.Default
    private Double scale = 0D;

    /**
     * Gets score.
     *
     * @return the score
     */
    public Double getScore() {
        if (this.score == 0L && !CollectionUtils.isEmpty(sections)) {
            this.score = sections.stream()
                    .map(quizSection -> quizSection.getScore())
                    .reduce(0D, Double::sum);
        }

        return this.score;
    }
}
