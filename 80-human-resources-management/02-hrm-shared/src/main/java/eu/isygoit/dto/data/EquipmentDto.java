package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.Data;

/**
 * The type Equipment dto.
 */
@Data

public class EquipmentDto extends AbstractAuditableDto<Long> {
    private Long id;
    private String type;
    private String description;


}
