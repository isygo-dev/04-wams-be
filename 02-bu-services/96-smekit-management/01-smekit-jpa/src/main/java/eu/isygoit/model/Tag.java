package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = SchemaTableConstantName.T_TAG)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag extends AuditableEntity<Long> {
    @Id
    @SequenceGenerator(
            name = "tag_sequence_generator",
            sequenceName = "tag_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "tag_sequence_generator")
    @Column(
            name = SchemaColumnConstantName.C_ID,
            updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(
            name = "tag_name",
            length = SchemaConstantSize.tags,
            unique = true,
            updatable = true, nullable = false)
    @EqualsAndHashCode.Include
    private String tagName;

//    @ManyToMany(mappedBy = "tags")
//    private Set<Template> templates = new HashSet<>();
//
    @ManyToMany(mappedBy = "tags")
    private Set<Category> categories = new HashSet<>();
//}
}
