package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Interview skills dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InterviewSkillsDto extends AbstractAuditableDto<Long> {

    private String type;
    private String name;
    private String level;
    @Builder.Default
    private Double score = 0D;
}
