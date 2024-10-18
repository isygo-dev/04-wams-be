package eu.isygoit.dto.data;

import eu.isygoit.dto.common.LinkedFileMinDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The type Employee linked file dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class EmployeeLinkedFileDto extends LinkedFileMinDto<Long> {

}
