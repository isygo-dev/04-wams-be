package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumIntegrationOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Integration order dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class IntegrationOrderDto extends AbstractAuditableDto<Long> {

    private String domain;
    private String code;
    private String name;
    private String description;
    private String serviceName;
    private String mapping;
    private IEnumIntegrationOrder.Types integrationOrder;
}
