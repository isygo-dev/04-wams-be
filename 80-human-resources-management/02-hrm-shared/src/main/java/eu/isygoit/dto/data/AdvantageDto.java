package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.Data;

/**
 * The type Advantage dto.
 */
@Data

public class AdvantageDto extends AbstractAuditableDto<Long> {
    private Long id;
    private String type;
    private String description;


}
