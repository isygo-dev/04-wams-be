package eu.isygoit.model;


import eu.isygoit.constants.DomainConstants;
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
import org.hibernate.annotations.ColumnDefault;

/**
 * The type Job offer template.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER_TEMPLATE)
public class JobOfferTemplate extends AuditableEntity<Long> implements ISAASEntity {

    @Id
    @SequenceGenerator(name = "job_temp_sequence_generator", sequenceName = "job_temp_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_temp_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    //@Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;

    @Column(name = SchemaColumnConstantName.C_TITLE)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY /* NO CASCADE */)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_TEMP_REF_JOB))
    private JobOffer jobOffer;
}
