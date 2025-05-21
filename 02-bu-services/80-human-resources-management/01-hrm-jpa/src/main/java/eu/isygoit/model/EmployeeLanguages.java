package eu.isygoit.model;

import eu.isygoit.enums.IEnumLanguageLevel;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

/**
 * The type Employee languages.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmployeeLanguages {

    @Column(name = SchemaColumnConstantName.C_LANGUAGE_CODE, length = ComSchemaConstantSize.S_NAME)
    private String languageName;

    @Builder.Default
    @ColumnDefault("'BEGINNER'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_LEVEL, length = IEnumLanguageLevel.STR_ENUM_SIZE, nullable = false)
    private IEnumLanguageLevel.Types level = IEnumLanguageLevel.Types.BEGINNER;
}
