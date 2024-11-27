package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Id code dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class IdCodeDto extends AbstractAuditableDto<Long> {

    private String code;
}
