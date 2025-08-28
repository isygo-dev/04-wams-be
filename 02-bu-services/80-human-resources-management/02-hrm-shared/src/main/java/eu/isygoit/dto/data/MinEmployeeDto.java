package eu.isygoit.dto.data;

import eu.isygoit.constants.AccountTypeConstants;
import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AuditableDto;
import eu.isygoit.enums.IEnumEnabledBinaryStatus;
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
public class MinEmployeeDto extends AuditableDto<Long> implements IImageUploadDto {

    private String tenant;
    private String code;
    private String firstName;
    private String lastName;
    private String email;
    private String imagePath;
    private Integer numberActiveContracts;
    private String accountCode;
    @Builder.Default
    private String functionRole = AccountTypeConstants.TENANT_USER;
    @Builder.Default
    private IEnumEnabledBinaryStatus.Types employeeStatus = IEnumEnabledBinaryStatus.Types.ENABLED;
}
