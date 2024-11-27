package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Quiz section dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class QuizSectionDto extends AbstractAuditableDto<Long> {

    private String name;
    private String description;
    private Integer order;
    private List<QuizQuestionDto> questions;
}
