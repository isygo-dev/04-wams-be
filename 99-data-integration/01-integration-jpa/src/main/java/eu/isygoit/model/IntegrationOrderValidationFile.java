package eu.isygoit.model;

import eu.isygoit.model.extendable.FileEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Integration order validation file.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE)
public class IntegrationOrderValidationFile extends FileEntity<Long> implements IFileEntity {

    @Id
    @SequenceGenerator(name = "order_validation_sequence_generator", sequenceName = "order_validation_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_validation_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_INTEGRATION_FILE,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_INTEGRATION_ORDER_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
}
