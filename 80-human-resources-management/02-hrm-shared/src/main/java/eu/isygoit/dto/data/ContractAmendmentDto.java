package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.Data;

import java.util.Date;

/**
 * The type Contract amendment dto.
 */
@Data

public class ContractAmendmentDto extends AbstractAuditableDto<Long> {

    private Long id;
    private String type;
    private String description;
    private Date date;
}
