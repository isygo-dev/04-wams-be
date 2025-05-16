package eu.isygoit.dto.data;

import eu.isygoit.enums.IEnumLanguageLevel;
import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * The type Employee languages dto.
 */
@Data

public class EmployeeLanguagesDto {

    private String languageName;
    @Nullable
    private IEnumLanguageLevel.Types level;
}


