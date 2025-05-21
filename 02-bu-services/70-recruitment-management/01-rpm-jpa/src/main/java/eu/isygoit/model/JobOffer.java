package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

/**
 * The type Job offer.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER,
        uniqueConstraints = {
                @UniqueConstraint(name = SchemaUcConstantName.UC_JOB_OFFER_CODE, columnNames = {SchemaColumnConstantName.C_CODE})
        })
@SQLDelete(sql = "update " + SchemaTableConstantName.T_JOB_OFFER + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class JobOffer extends AuditableCancelableEntity<Long> implements IDomainAssignable, ICodeAssignable, IMultiFileEntity<JobOfferLinkedFile> {

    @Id
    @SequenceGenerator(name = "job_offer_sequence_generator", sequenceName = "job_offer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_offer_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    //@Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;
    //@Convert(converter = LowerCaseConverter.class)
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    @Column(name = SchemaColumnConstantName.C_TITLE, length = SchemaConstantSize.XL_VALUE, nullable = false)
    private String title;
    @Column(name = SchemaColumnConstantName.C_DEPARTMENT)
    private String department;
    @Column(name = SchemaColumnConstantName.C_INDUSTRY)
    private String industry;
    @Column(name = SchemaColumnConstantName.C_EMPLOYER_TYPE, length = SchemaConstantSize.L_VALUE)
    private String employerType;
    @Column(name = SchemaColumnConstantName.C_JOB_FUNCTION, length = SchemaConstantSize.L_VALUE)
    private String jobFunction;
    @Column(name = SchemaColumnConstantName.C_OWNER, length = SchemaConstantSize.CUSTOMER)
    private String owner;
    @Column(name = SchemaColumnConstantName.C_CUSTOMER)
    private String customer;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB_DETAILS, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_REF_DETAILS))
    private JobOfferDetails details;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SHARED_WITH_REF_JOB))
    private List<JobOfferShareInfo> jobShareInfos;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_ADDITIONAL_FILE_REF_JOB))
    private List<JobOfferLinkedFile> additionalFiles;
}
