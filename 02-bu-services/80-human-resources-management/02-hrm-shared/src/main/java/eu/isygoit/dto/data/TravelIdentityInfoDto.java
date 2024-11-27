package eu.isygoit.dto.data;

import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Travel identity info dto.
 */
@Data

public class TravelIdentityInfoDto extends AbstractAuditableDto<Long> implements IImageUploadDto {

    private String cardNumber;
    private LocalDate issuedDate;
    private LocalDate expiredDate;
    private String issuedPlace;
    private String imagePath;
    private String domain;
    private Long employeeDetailsId;
    private String code;
}
