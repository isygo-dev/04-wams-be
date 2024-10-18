package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Quiz section report dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizSectionReportDto extends AbstractAuditableDto<Long> {

    private String name;
    private String description;
    @Builder.Default
    private Double score = 0D;
    @Builder.Default
    private Double scale = 0D;
}
