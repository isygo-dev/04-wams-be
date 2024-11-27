package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Professional information.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_EMPLOYEE_PROF_INFORMATION)
public class ProfessionalInformation extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "profInfo_sequence_generator", sequenceName = "prof_info_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profInfo_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_JOB_TITLE, length = ComSchemaConstantSize.M_VALUE)
    private String jobTitle;

}
