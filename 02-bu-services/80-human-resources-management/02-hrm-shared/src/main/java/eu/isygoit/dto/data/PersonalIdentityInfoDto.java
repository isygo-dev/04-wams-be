package eu.isygoit.dto.data;

import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AuditableDto;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Personal identity info dto.
 */
@Data

public class PersonalIdentityInfoDto extends AuditableDto<Long> implements IImageUploadDto {

    private String cardNumber;
    private LocalDate issuedDate;
    private String issuedPlace;
    private String imagePath;
    private String tenant;
    private Long employeeDetailsId;
    private String code;
}
