package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumTempCategoryStatus;

import java.util.List;

public class TempCategoryDto extends AbstractAuditableDto<Long> {
    private String domain;
    private String name;

    private String description;
    private IEnumTempCategoryStatus.Types type = IEnumTempCategoryStatus.Types.DISABLED;
    private List<TemplateDto> templates ;

}
