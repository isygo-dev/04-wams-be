package eu.isygoit.model;

import eu.isygoit.enums.IEnumEducationalLevel;
import eu.isygoit.enums.IEnumGender;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Children information.
 */
@Embeddable
@Data
public class ChildrenInformation {

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_GENDER, length = IEnumGender.STR_ENUM_SIZE)
    private IEnumGender.Types gender;
    @Column(name = SchemaColumnConstantName.C_BIRTHDAY)
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_EDUCATION_LEVEL, length = IEnumEducationalLevel.STR_ENUM_SIZE)
    private IEnumEducationalLevel.Types educationalLevel;
}
