package eu.isygoit.dto.data;

import eu.isygoit.constants.AccountTypeConstants;
import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumBinaryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Min employee dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MinEmployeeDto extends AbstractAuditableDto<Long> implements IImageUploadDto {

    private String domain;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String imagePath;
    private Integer numberActiveContracts;
    private String accountCode;
    @Builder.Default
    private String functionRole = AccountTypeConstants.DOMAIN_USER;
    @Builder.Default
    private IEnumBinaryStatus.Types employeeStatus = IEnumBinaryStatus.Types.ENABLED;
}
