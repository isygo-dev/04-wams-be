package eu.isygoit.dto.filter;

import eu.isygoit.dto.extendable.IdentifiableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Resume filter dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResumeFilterDto extends IdentifiableDto<Long> {

    private String domain;
    private String firstName;
    private String lastName;
}
