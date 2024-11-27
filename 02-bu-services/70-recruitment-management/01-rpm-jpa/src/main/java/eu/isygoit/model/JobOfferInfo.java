package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * The type Job offer info.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER_INFO)
public class JobOfferInfo extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "resume_details_sequence_generator", sequenceName = "resume_details_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_details_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_START_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = SchemaColumnConstantName.C_END_DATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = SchemaColumnConstantName.C_DEADLINE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;
    @Column(name = SchemaColumnConstantName.C_POSITIONS)
    private String position;
    @Column(name = SchemaColumnConstantName.C_EDUCATION_LEVEL, length = SchemaConstantSize.LEVEL)
    private String educationLevel;
    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_JOB_OFFER_QUALIFICATIONS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_JOB,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_JOB)))
    @Column(name = SchemaColumnConstantName.C_TAG)
    private List<String> qualifications;

}
