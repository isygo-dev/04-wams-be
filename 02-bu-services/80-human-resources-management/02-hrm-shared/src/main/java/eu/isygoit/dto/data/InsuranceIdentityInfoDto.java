package eu.isygoit.dto.data;

import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AuditableDto;
import eu.isygoit.enums.IEnumInsuranceType;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Insurance identity info dto.
 */
@Data

public class InsuranceIdentityInfoDto extends AuditableDto<Long> implements IImageUploadDto {

    private String cardNumber;
    private LocalDate issuedDate;
    private LocalDate expiredDate;
    private String issuedPlace;
    private IEnumInsuranceType.Types insuranceType;
    private String imagePath;
    private String tenant;
    private Long employeeDetailsId;
    private String code;
}
