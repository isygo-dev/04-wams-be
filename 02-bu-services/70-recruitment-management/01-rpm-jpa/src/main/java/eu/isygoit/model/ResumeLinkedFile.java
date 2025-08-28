package eu.isygoit.model;

import eu.isygoit.constants.TenantConstants;
import eu.isygoit.converter.LowerCaseConverter;
import eu.isygoit.model.extendable.LinkedFileModel;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

/**
 * The type Resume linked file.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_RESUME_LINKED_FILE)
public class ResumeLinkedFile extends LinkedFileModel<Long> implements ITenantAssignable {

    @Id
    @SequenceGenerator(name = "resume_multi_file_sequence_generator", sequenceName = "resume_multi_file_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_multi_file_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + TenantConstants.DEFAULT_TENANT_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_TENANT, length = SchemaConstantSize.TENANT, updatable = false, nullable = false)
    private String tenant;
}
