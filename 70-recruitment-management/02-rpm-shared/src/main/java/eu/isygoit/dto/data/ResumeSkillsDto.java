package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumSkillLevelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Resume skills dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSkillsDto extends AbstractAuditableDto<Long> {

    private String name;
    @Builder.Default
    private IEnumSkillLevelType.Types level = IEnumSkillLevelType.Types.BEGINNER;
    @Builder.Default
    private Double score = 0D;
}
