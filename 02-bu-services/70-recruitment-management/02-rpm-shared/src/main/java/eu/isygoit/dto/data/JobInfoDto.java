package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * The type Job info dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JobInfoDto extends AbstractAuditableDto<Long> {

    private Date startDate;
    private Date endDate;
    private Date deadline;
    private String position;
    private String educationLevel;
    private List<String> qualifications;
}
