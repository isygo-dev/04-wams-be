package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * The type Resume prof experience dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeProfExperienceDto extends AbstractAuditableDto<Long> {

    private String jobTitle;
    private String employer;
    private String city;
    private String country;
    private Date startDate;
    private Date endDate;
    private Boolean workhere;

    private String description;
    private List<String> technology;
}
