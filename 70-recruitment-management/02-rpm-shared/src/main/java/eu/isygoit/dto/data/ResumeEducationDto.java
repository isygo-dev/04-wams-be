package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * The type Resume education dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeEducationDto extends AbstractAuditableDto<Long> {

    private String institution;
    private String city;
    private String qualification;
    private String fieldOfStudy;
    private Date yearOfGraduation;
    private String country;
}
