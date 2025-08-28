package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * The type Integration flow dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class IntegrationFlowDto extends AuditableDto<Long> {

    private String tenant;
    private String code;
    private String orderName;
    private Date integrationDate;
}
