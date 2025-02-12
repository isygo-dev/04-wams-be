package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumTempCategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class TempCategoryDto extends AbstractAuditableDto<Long> {
    private String domain;
    private String name;

    private String description;
    private IEnumTempCategoryStatus.Types type = IEnumTempCategoryStatus.Types.DISABLED;
 //   private List<TemplateDto> templates ;

}
