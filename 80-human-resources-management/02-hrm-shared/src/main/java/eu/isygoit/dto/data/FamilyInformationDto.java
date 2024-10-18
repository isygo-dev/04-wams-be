package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;


/**
 * The type Family information dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FamilyInformationDto extends AbstractAuditableDto<Long> {

    private String spouseName;
    private Integer numberOfChildren;
    private Set<ChildrenInformationDto> childrenInformations;
}
