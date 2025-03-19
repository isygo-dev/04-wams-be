package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
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
public class Author extends AuditableCancelableEntity <Long> implements ICodifiable ,ISAASEntity,IImageEntity {
    @Id
    @SequenceGenerator(name = "author_sequence_generator", sequenceName = "author_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_sequence_generator")
    @Column(name = ComSchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = ComSchemaColumnConstantName.C_DOMAIN, length = ComSchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private  String domain;
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    @Column(name = ComSchemaColumnConstantName.C_PHOTO)
    private String imagePath;
    @Column(name = ComSchemaColumnConstantName.C_FIRST_NAME, length = ComSchemaConstantSize.S_NAME, updatable = true, nullable = false)
    private String firstname;

    @Column(name = ComSchemaColumnConstantName.C_LAST_NAME, length = ComSchemaConstantSize.S_NAME, updatable = true, nullable = true)
    private String lastname;

    @Column(name = ComSchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;




}

