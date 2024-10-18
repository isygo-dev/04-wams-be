package eu.isygoit.dto.data;

import eu.isygoit.enums.IEnumEducationalLevel;
import eu.isygoit.enums.IEnumGender;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Children information dto.
 */
@Data

public class ChildrenInformationDto {
    private IEnumGender.Types gender;
    private LocalDate birthDate;
    private String fullName;
    private IEnumEducationalLevel.Types educationalLevel;
}
