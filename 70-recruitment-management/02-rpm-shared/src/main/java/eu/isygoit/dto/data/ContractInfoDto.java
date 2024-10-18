package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumContractType;
import eu.isygoit.enums.IEnumTimeType;
import eu.isygoit.enums.IEnumWorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Contract info dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ContractInfoDto extends AbstractAuditableDto<Long> {

    private String location;
    private Integer salaryMin;
    private Integer salaryMax;
    private IEnumWorkMode.Types workingMode;
    private IEnumContractType.Types contract;
    private IEnumTimeType.Types availability;
    private String currency;
}


