package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Table(name = SchemaTableConstantName.T_AUTHOR)

@SQLDelete(sql = "update " + SchemaTableConstantName.T_AUTHOR + " set " + ComSchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = ComSchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class Author extends AuditableCancelableEntity <Long> implements ICodifiable {
    @Id
    @SequenceGenerator(name = "author_sequence_generator", sequenceName = "author_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_sequence_generator")
    @Column(name = ComSchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;

    @Column(name = ComSchemaColumnConstantName.C_NAME, length = ComSchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Column(name = ComSchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    @Column(name = SchemaColumnConstantName.A_URL, length = SchemaConstantSize.URL)
    private String url;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn(name = SchemaColumnConstantName.C_AUTH, referencedColumnName = ComSchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_REF_AUTHOR))
    private List<Template> templates;
}

