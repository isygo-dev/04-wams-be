package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * The type Candidate quiz dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CandidateQuizDto extends AbstractAuditableDto<Long> {

    private String accountCode;
    private String quizCode;
    private String name;
    private String description;
    private Date startDate;
    private Date submitDate;
    private List<String> tags;
    private Long score;
}
