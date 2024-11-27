package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Quiz report dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizReportDto extends AbstractAuditableDto<Long> {

    private String quizCode;
    private String accountCode;

    private String name;
    private String description;
    private Date startDate;
    private Date submitDate;
    @Builder.Default
    private List<QuizSectionReportDto> sectionReports = new ArrayList<>();
    @Builder.Default
    private Double score = 0D;
    @Builder.Default
    private Double scale = 0D;
    private List<String> tags;

    /**
     * Gets scale.
     *
     * @return the scale
     */
    public Double getScale() {
        return sectionReports.stream()
                .map(quizSection -> quizSection.getScale())
                .reduce(0D, Double::sum);
    }
}
