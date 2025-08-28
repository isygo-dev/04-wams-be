package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
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
public class InterviewSkillsDto extends AuditableDto<Long> {

    private String type;
    private String name;
    private String level;
    @Builder.Default
    private Double score = 0D;
}
