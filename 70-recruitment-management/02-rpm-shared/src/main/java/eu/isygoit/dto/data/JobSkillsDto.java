package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumSkillLevelType;
import eu.isygoit.enums.IEnumSkillType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Job skills dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JobSkillsDto extends AbstractAuditableDto<Long> {
    private Long id;
    private IEnumSkillType.Types type;
    private String name;
    private IEnumSkillLevelType.Types level;
    private Boolean isMandatory;
}
