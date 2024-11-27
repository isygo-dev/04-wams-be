package eu.isygoit.dto.data;

import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Personal identity info dto.
 */
@Data

public class PersonalIdentityInfoDto extends AbstractAuditableDto<Long> implements IImageUploadDto {

    private String cardNumber;
    private LocalDate issuedDate;
    private String issuedPlace;
    private String imagePath;
    private String domain;
    private Long employeeDetailsId;
    private String code;
}
