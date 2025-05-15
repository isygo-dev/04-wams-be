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

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = SchemaTableConstantName.T_AUTHOR)
@SecondaryTable(name = SchemaTableConstantName.T_AUTHOR_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID))
@SQLDelete(sql = "UPDATE " + SchemaTableConstantName.T_AUTHOR + " SET " +
        ComSchemaColumnConstantName.C_CHECK_CANCEL + " = true, " +
        ComSchemaColumnConstantName.C_CANCEL_DATE + " = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = ComSchemaColumnConstantName.C_CHECK_CANCEL + " = false")
public class Author extends AuditableCancelableEntity<Long> implements ICodifiable, ISAASEntity, IImageEntity, IFileEntity {

    @Id
    @SequenceGenerator(name = "author_sequence_generator", sequenceName = "author_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_sequence_generator")
    @Column(name = ComSchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = ComSchemaColumnConstantName.C_DOMAIN, length = ComSchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String domain;

    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;

    @Column(name = ComSchemaColumnConstantName.C_PHOTO)
    private String imagePath;

    @Column(name = ComSchemaColumnConstantName.C_FIRST_NAME, length = ComSchemaConstantSize.S_NAME, nullable = false)
    private String firstname;

    @Column(name = ComSchemaColumnConstantName.C_LAST_NAME, length = ComSchemaConstantSize.S_NAME)
    private String lastname;

    @Column(name = ComSchemaColumnConstantName.C_EMAIL, length = ComSchemaConstantSize.EMAIL)
    private String email;

    @Column(name = ComSchemaColumnConstantName.C_PHONE_NUMBER, length = ComSchemaConstantSize.PHONE_NUMBER)
    private String phone;

    // IFileEntity fields
    @Column(name = SchemaColumnConstantName.C_FILE_NAME, table = SchemaTableConstantName.T_AUTHOR_FILE)
    private String fileName;

    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_AUTHOR_FILE)
    private String originalFileName;

    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_PATH, table = SchemaTableConstantName.T_AUTHOR_FILE)
    private String path;

    @Column(name = SchemaColumnConstantName.C_EXTENSION, table = SchemaTableConstantName.T_AUTHOR_FILE)
    private String extension;

    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_AUTHOR_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_AUTHOR_FILE_TAGS,
            joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_AUTHOR,
                    referencedColumnName = SchemaColumnConstantName.C_ID,
                    foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_AUTHOR_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags = new ArrayList<>();
}