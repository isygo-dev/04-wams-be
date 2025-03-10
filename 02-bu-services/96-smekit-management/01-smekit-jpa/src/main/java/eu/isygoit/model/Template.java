package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.*;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = SchemaTableConstantName.T_TEMPLATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SecondaryTable(name = SchemaTableConstantName.T_TEMPLATE_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID))
public  class Template  extends AuditableEntity<Long> implements IFileEntity,ICodifiable, ISAASEntity{
    @Id
    @SequenceGenerator(
            name = "template_sequence_generator",
            sequenceName = "template_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "template_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private  String domain;
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    @Column(name = SchemaColumnConstantName.C_PATH, length = ComSchemaConstantSize.PATH)
    private String path ;

    @Column(name = SchemaColumnConstantName.C_FILE_NAME, length = ComSchemaConstantSize.FILE_NAME)
    private String fileName ;

    @Column(name = SchemaColumnConstantName.C_EXTENSION, length = ComSchemaConstantSize.EXTENSION_SIZE)
    private String extension;

    private LocalDateTime editionDate;

    @Column(name = SchemaColumnConstantName.C_SOURCE, length = SchemaConstantSize.SOURCE)
    private String source;

    @Column(name = SchemaColumnConstantName.C_VERSION, length = SchemaConstantSize.VERSION)
    private String version;



    @Builder.Default
    @ColumnDefault("'EDITING'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.T_S, length = IEnumDocTempStatus.STR_ENUM_SIZE, nullable = false)
    private IEnumDocTempStatus.Types typeTs = IEnumDocTempStatus.Types.EDITING;


    @Builder.Default
    @ColumnDefault("'PRV'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.T_V, length = IEnumTemplateVisibility.STR_ENUM_SIZE, nullable = false)
    private IEnumTemplateVisibility.Types typeTv = IEnumTemplateVisibility.Types.PRV;

    @Builder.Default
    @ColumnDefault("'EN'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.T_L, length = IEnumTemplateLanguage.STR_ENUM_SIZE, nullable = false)
    private IEnumTemplateLanguage.Types typeTl = IEnumTemplateLanguage.Types.EN;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name =SchemaColumnConstantName.C_DOC , referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_DOCUMENTS))
    private List<Document> documents;

    @ManyToOne
    @JoinColumn(name = SchemaColumnConstantName.C_AUTH, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_REF_AUTHOR))
    private Author author;

    @ManyToOne
    @JoinColumn(name = SchemaColumnConstantName.C_CAT, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_CATEGORY))

    private Category category;


    @ManyToMany
    @JoinTable(
            name = SchemaColumnConstantName.C_TEMP_T,
            joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_TEMPLATE_ID, referencedColumnName = SchemaColumnConstantName.C_ID),
            inverseJoinColumns = @JoinColumn(name = SchemaColumnConstantName.C_TAG_ID, referencedColumnName = ComSchemaColumnConstantName.C_ID)
    )
    private List<Tag> tags;





    //BEGIN IFileEntity : SecondaryTable / ResumeFile

    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_TEMPLATE_FILE)
    private String originalFileName;
    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_TEMPLATE_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_TEMPLATE_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_TEMPLATE,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_TEMPLATE_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> TemplatesTags;
    //END IFileEntity : SecondaryTable

    @Override
    public List<String> getTags() {
        if (tags == null) {
            return Collections.emptyList();
        }
        return tags.stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
    }

}
