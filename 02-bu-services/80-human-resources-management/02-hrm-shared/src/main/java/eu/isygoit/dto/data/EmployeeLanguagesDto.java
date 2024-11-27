package eu.isygoit.dto.data;

import eu.isygoit.enums.IEnumLanguageLevelType;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * The type Employee languages dto.
 */
@Data

public class EmployeeLanguagesDto {

    private String languageName;
    @Nullable
    private IEnumLanguageLevelType.Types level;
}


