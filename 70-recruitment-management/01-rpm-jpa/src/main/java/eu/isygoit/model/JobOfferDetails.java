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

import java.util.List;

/**
 * The type Job offer details.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER_DETAILS)
public class JobOfferDetails extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "resume_details_sequence_generator", sequenceName = "resume_details_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_details_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = SchemaConstantSize.DESCRIPTION)
    private String description;
    @Column(name = SchemaColumnConstantName.C_EXPERIENCE_MIN)
    private Integer experienceMin;
    @Column(name = SchemaColumnConstantName.C_EXPERIENCE_MAX)
    private Integer experienceMax;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_JOB_OFFER_RESPONSIBILITIES,
            joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_JOB,
                    referencedColumnName = SchemaColumnConstantName.C_ID,
                    foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_RESPONSABILITY_REF_JOB)))
    @Column(name = SchemaColumnConstantName.C_RESPONSABILITY)
    private List<String> responsibility;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB_DETAILS, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_HARD_SKILLS_REF_JOB))
    private List<JobOfferHardSkills> hardSkills;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB_DETAILS, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SOFT_SKILLS_REF_JOB))
    private List<JobOfferSoftSkills> softSkills;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB_INFO, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_REF_JOBINFO))
    private JobOfferInfo jobInfo;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_CONTRACT_INFO, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_REF_CONTRACT_INFO))
    private ContractInfo contractInfo;
}
