package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * The type Resume education.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_RESUME_EDUCATION)
public class ResumeEducation extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "resume_educ_sequence_generator", sequenceName = "resume_educ_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_educ_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_INSTITUTION, length = ComSchemaConstantSize.S_NAME)
    private String institution;
    @Column(name = SchemaColumnConstantName.C_CITY_NAME, length = ComSchemaConstantSize.CITY_NAME)
    private String city;
    @Column(name = SchemaColumnConstantName.C_QUALIFICATION, length = ComSchemaConstantSize.QUALIFICATION)
    private String qualification;
    @Column(name = SchemaColumnConstantName.C_FIELD_NAME, length = ComSchemaConstantSize.FILE_NAME)
    private String fieldOfStudy;
    @Column(name = SchemaColumnConstantName.C_DATE_GRADUATION)
    @Temporal(TemporalType.TIMESTAMP)
    private Date yearOfGraduation;
    @Column(name = ComSchemaColumnConstantName.C_COUNTRY_NAME, length = SchemaConstantSize.S_NAME)
    private String country;
}
