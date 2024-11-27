package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

/**
 * The type Resume prof experience.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_RESUME_PROF_EXPERIENCE)
public class ResumeProfExperience extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "resume_prof_exp_sequence_generator", sequenceName = "resume_prof_exp_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_prof_exp_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_JOB_TITLE, length = ComSchemaConstantSize.S_NAME)
    private String jobTitle;
    @Column(name = SchemaColumnConstantName.C_EMPLOYER, length = ComSchemaConstantSize.S_NAME)
    private String employer;
    @Column(name = SchemaColumnConstantName.C_CITY_NAME, length = ComSchemaConstantSize.CITY_NAME)
    private String city;
    @Column(name = SchemaColumnConstantName.C_COUNTRY_NAME, length = ComSchemaConstantSize.COUNTRY_NAME)
    private String country;
    @Column(name = SchemaColumnConstantName.C_START_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = SchemaColumnConstantName.C_END_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Builder.Default
    @ColumnDefault("'false'")
    @Column(name = SchemaColumnConstantName.C_WORKHERE, nullable = false)
    private Boolean workhere = Boolean.FALSE;
    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    @ElementCollection
    @CollectionTable(name = SchemaColumnConstantName.T_PROF_EXP_TECHNO
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_PROF_EXP,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaColumnConstantName.FK_PROF_EXP_TECHNO_PROF_EXP)))
    @Column(name = SchemaColumnConstantName.C_TECHNO)
    private List<String> technology;


}
