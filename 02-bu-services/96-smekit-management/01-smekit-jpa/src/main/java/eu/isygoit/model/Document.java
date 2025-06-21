package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumDocTempStatus;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = SchemaTableConstantName.T_DOCUMENT)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SecondaryTable(
        name = SchemaTableConstantName.T_DOCUMENT_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID)
)
public class Document extends AuditableEntity<Long> implements ISAASEntity, ICodifiable, IFileEntity {
    @Id
    @EqualsAndHashCode.Include
    @SequenceGenerator(
            name = "document_sequence_generator",
            sequenceName = "document_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "document_sequence_generator")
    @Column(
            name = SchemaColumnConstantName.C_ID,
            updatable = false, nullable = false)
    private Long id;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String domain;

    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    @Column(name = "dms_file_id")
    private String dmsFileId;
    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = true, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    private LocalDateTime editionDate;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder.Default
    @ColumnDefault("'false'")
    private Boolean shared = Boolean.FALSE;

    @Column(name = "is_template_copy", nullable = false)
    @Builder.Default
    private Boolean isTemplateCopy = false;

    @Column(name = "original_document_id")
    private Long originalDocumentId;


    @Builder.Default
    @ColumnDefault("'EDITING'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.D_S, length = IEnumDocTempStatus.STR_ENUM_SIZE, nullable = false)
    private IEnumDocTempStatus.Types tempType = IEnumDocTempStatus.Types.EDITING;

    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_DOCUMENT_FILE)
    private String type;

    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_DOCUMENT_FILE)
    private String originalFileName;

    @Column(name = SchemaColumnConstantName.C_FILE_NAME, length = ComSchemaConstantSize.FILE_NAME, table = SchemaTableConstantName.T_DOCUMENT_FILE)
    private String fileName;

    @Column(name = SchemaColumnConstantName.C_PATH, length = SchemaConstantSize.PATH_TEMPLATE_FILE, table = SchemaTableConstantName.T_DOCUMENT_FILE)
    private String path;

    @Column(name = SchemaColumnConstantName.C_EXTENSION, length = ComSchemaConstantSize.EXTENSION_SIZE, table = SchemaTableConstantName.T_DOCUMENT_FILE)
    private String extension;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_SH_WTH, referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_DOCUMENTS_SHAREDWITH))
    @Builder.Default
    private Set<SharedWith> sharedWithUsers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_COMMENT, referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_DOCUMENTS_COMMENTS))
    @Builder.Default
    private Set<DocComment> comments = new HashSet<>();

    @Override
    public List<String> getTags() {
        return Collections.emptyList();
    }
}




