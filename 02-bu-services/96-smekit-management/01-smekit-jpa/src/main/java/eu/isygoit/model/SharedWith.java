package eu.isygoit.model;

import eu.isygoit.enums.IEnumPermissionLevel;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

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

    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, nullable = false, updatable = false)
    private String user;


    @ManyToOne
    @JoinColumn(name = SchemaColumnConstantName.C_DOCUMENT_ID, foreignKey = @ForeignKey(name = "fk_sharedwith_document"))
    private   Document document;

    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_PERMISSION, nullable = false)
    private IEnumPermissionLevel.PermissionLevel permission;
}
