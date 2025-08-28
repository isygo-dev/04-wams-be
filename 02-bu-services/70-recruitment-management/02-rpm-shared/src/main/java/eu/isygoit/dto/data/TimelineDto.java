package eu.isygoit.dto.data;

import eu.isygoit.dto.IExchangeObjectDto;
import eu.isygoit.dto.extendable.AuditableDto;
import eu.isygoit.enums.IEnumActionEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Timeline dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineDto extends AuditableDto<Long> implements IExchangeObjectDto {

    private String tenant;
    private IEnumActionEvent.Types action;
    private String code;
    private String object;
    private String parentCode;
}

