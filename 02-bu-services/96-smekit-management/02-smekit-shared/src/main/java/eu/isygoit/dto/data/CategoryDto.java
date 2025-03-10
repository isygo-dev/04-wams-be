package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumCategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class CategoryDto extends AbstractAuditableDto<Long> {
    private String domain;
    private String name;
    private String description;
    private IEnumCategoryStatus.Status type = IEnumCategoryStatus.Status.DISABLED;
//    private List<TemplateDto> templates ;

}
