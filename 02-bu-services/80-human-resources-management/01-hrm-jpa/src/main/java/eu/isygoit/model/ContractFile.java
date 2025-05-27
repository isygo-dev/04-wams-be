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
 * The type Contract file.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_CONTRACT_FILE)
public class ContractFile extends FileEntity<Long> implements IFileEntity {

    @Id
    @SequenceGenerator(name = "contract_file_sequence_generator", sequenceName = "contract_file_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_file_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_CONTRACT_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_CONTRACT,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAG_REF_CONTRACT_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
}
