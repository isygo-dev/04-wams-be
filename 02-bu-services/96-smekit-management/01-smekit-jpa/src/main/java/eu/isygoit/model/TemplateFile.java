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

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_TEMPLATE_FILE)
public class TemplateFile  extends FileEntity<Long> implements IFileEntity  {
    @Id
    @SequenceGenerator(name = "template_file_sequence_generator", sequenceName = "template_file_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_file_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_TEMPLATE_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_TEMPLATE,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_TEMPLATE_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
}
