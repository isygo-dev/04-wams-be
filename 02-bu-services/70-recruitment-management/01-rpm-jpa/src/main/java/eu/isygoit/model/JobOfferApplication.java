package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.converter.LowerCaseConverter;
import eu.isygoit.enums.IEnumEnabledBinaryStatus;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Job offer application.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER_APPLICATION, uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_APPLICATION_CODE,
                columnNames = {SchemaColumnConstantName.C_CODE}),
        @UniqueConstraint(name = SchemaUcConstantName.UC_APP_RESUME_JOB,
                columnNames = {SchemaColumnConstantName.C_RESUME, SchemaColumnConstantName.C_JOB})
})
@SQLDelete(sql = "update " + SchemaTableConstantName.T_JOB_OFFER_APPLICATION + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class JobOfferApplication extends AuditableCancelableEntity<Long> implements IDomainAssignable, ICodeAssignable, IStatusAssignable<String>, IBoardItem<String> {

    @Id
    @SequenceGenerator(name = "job_appli_sequence_generator", sequenceName = "job_appli_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_appli_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;

    @Convert(converter = LowerCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, unique = true, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_STATE)
    private String state;

    @ManyToOne(fetch = FetchType.LAZY/* NO CASCADE */)
    @JoinColumn(name = SchemaColumnConstantName.C_RESUME, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_APP_REF_RESUME))
    private Resume resume;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ENABLED'")
    @Column(name = SchemaColumnConstantName.C_JOB_APPLICATION_STATUS, length = IEnumEnabledBinaryStatus.STR_ENUM_SIZE)
    private IEnumEnabledBinaryStatus.Types binaryStatusType = IEnumEnabledBinaryStatus.Types.ENABLED;
    @ManyToOne(fetch = FetchType.LAZY /* NO CASCADE */)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_APP_REF_JOB))
    private JobOffer jobOffer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB_APPLICATION, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_APP_REF_JOB_APP_EVENT))
    private List<JobOfferApplicationEvent> jobApplicationEvents;


    @Override
    public String getItemName() {
        return (this.getResume() != null && this.getJobOffer() != null) ?
                new StringBuilder(this.getResume().getFullName()).append(" Applied for ")
                        .append(this.getJobOffer().getTitle()).toString()
                : "Missing Job application details";
    }

    @Override
    public String getImagePath() {
        return resume.getImagePath();
    }

    @Override
    public List<IBoardEvent> getEvents() {
        return new ArrayList<>(jobApplicationEvents);
    }

    @Override
    public String getItemImage() {
        return resume != null ? resume.getId().toString() : null;
    }
}
