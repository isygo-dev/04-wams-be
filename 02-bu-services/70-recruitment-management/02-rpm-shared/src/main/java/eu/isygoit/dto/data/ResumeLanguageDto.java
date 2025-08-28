package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import eu.isygoit.enums.IEnumLanguageLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Resume language dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeLanguageDto extends AuditableDto<Long> {

    private String name;
    private IEnumLanguageLevel.Types level;
}
