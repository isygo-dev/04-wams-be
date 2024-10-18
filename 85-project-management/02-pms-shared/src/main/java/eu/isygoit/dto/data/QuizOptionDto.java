package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The type Quiz option dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class QuizOptionDto extends AbstractAuditableDto<Long> {

    private String option;
    private Boolean checked;
}
