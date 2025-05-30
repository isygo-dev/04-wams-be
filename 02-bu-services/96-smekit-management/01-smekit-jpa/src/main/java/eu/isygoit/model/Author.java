package eu.isygoit.model;

import eu.isygoit.annotation.Criteria;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.converter.CamelCaseConverter;
import eu.isygoit.converter.LowerCaseConverter;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = SchemaTableConstantName.T_AUTHOR)
@SecondaryTable(name = SchemaTableConstantName.T_AUTHOR_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID))
@SQLDelete(sql = "UPDATE " + SchemaTableConstantName.T_AUTHOR + " SET " +
        SchemaColumnConstantName.C_CHECK_CANCEL + " = true, " +
        SchemaColumnConstantName.C_CANCEL_DATE + " = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + " = false")
public class Author extends AuditableCancelableEntity<Long> implements ICodeAssignable, IDomainAssignable, IImageEntity, IFileEntity {

    @Id
    @SequenceGenerator(name = "author_sequence_generator", sequenceName = "author_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;

    @Convert(converter = LowerCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, unique = true, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_PHOTO)
    private String imagePath;

    @Convert(converter = CamelCaseConverter.class)
    @Criteria
    @Column(name = SchemaColumnConstantName.C_FIRST_NAME, length = SchemaConstantSize.S_NAME, nullable = false)
    private String firstName;

    @Convert(converter = CamelCaseConverter.class)
    @Criteria
    @Column(name = SchemaColumnConstantName.C_LAST_NAME, length = SchemaConstantSize.S_NAME, nullable = false)
    private String lastName;

    @Column(name = SchemaColumnConstantName.C_EMAIL, length = SchemaConstantSize.EMAIL)
    private String email;

    @Column(name = SchemaColumnConstantName.C_PHONE_NUMBER, length = SchemaConstantSize.PHONE_NUMBER)
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
                    referencedColumnName = SchemaColumnConstantName.C_CODE,
                    foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAG_REF_AUTHOR_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG)
    private List<String> tags = new ArrayList<>();
}