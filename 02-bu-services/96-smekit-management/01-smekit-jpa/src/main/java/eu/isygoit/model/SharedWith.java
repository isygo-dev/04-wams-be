package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = SchemaTableConstantName.T_SHAREDWITH)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SharedWith extends AuditableEntity<Long> {
    @Id
    @SequenceGenerator(name = "sharedwith_sequence_generator", sequenceName = "sharedwith_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sharedwith_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String userName;

    @ManyToOne
    @JoinColumn(name = SchemaColumnConstantName.C_CAT, foreignKey = @ForeignKey(name = "fk_template_category"))
    private Document document;
}
