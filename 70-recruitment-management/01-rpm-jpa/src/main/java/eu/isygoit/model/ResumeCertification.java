package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * The type Resume certification.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_RESUME_CERTIFICATION)
public class ResumeCertification extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "resume_certif_sequence_generator", sequenceName = "resume_certif_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_certif_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;
    @Column(name = SchemaColumnConstantName.C_LINK, length = ComSchemaConstantSize.LINK)
    private String link;
    @Column(name = SchemaColumnConstantName.C_DATE_OBTAINED)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfObtained;
}
