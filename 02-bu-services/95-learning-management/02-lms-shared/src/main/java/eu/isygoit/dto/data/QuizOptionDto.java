package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Quiz option dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class QuizOptionDto extends AuditableDto<Long> {

    private String option;
    private Boolean checked;
}
