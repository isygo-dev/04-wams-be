package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.Data;

/**
 * The type Equipment dto.
 */
@Data

public class EquipmentDto extends AuditableDto<Long> {
    private Long id;
    private String type;
    private String description;


}
