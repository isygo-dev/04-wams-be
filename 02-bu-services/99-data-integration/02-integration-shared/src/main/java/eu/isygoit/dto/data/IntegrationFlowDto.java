package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
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
public class IntegrationFlowDto extends AbstractAuditableDto<Long> {

    private String domain;
    private String code;
    private String orderName;
    private Date integrationDate;
}
