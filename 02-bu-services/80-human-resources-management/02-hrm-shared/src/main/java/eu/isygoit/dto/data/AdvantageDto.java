package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.Data;

/**
 * The type Advantage dto.
 */
@Data

public class AdvantageDto extends AuditableDto<Long> {
    private Long id;
    private String type;
    private String description;


}
