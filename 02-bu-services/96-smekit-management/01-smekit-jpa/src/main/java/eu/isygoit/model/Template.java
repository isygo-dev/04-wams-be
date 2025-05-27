package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumLanguage;
import eu.isygoit.enums.IEnumTemplateStatus;
import eu.isygoit.enums.IEnumTemplateVisibility;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = SchemaTableConstantName.T_TEMPLATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SecondaryTable(name = SchemaTableConstantName.T_TEMPLATE_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID))
public class Template extends AuditableEntity<Long> implements IFileEntity, ICodeAssignable, IDomainAssignable, IStatusAssignable<IEnumTemplateStatus.Types> {
    @Id
    @SequenceGenerator(name = "template_sequence_generator", sequenceName = "template_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "template_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String domain;

    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = SchemaConstantSize.DESCRIPTION)
    private String description;

    @Column(name = SchemaColumnConstantName.C_PATH, length = SchemaConstantSize.PATH_TEMPLATE_FILE)
    private String path;

    @Column(name = SchemaColumnConstantName.C_FILE_NAME, length = SchemaConstantSize.FILE_NAME)
    private String fileName;

    @Column(name = SchemaColumnConstantName.C_EXTENSION, length = SchemaConstantSize.EXTENSION_SIZE)
    private String extension;

    @Column(name = SchemaColumnConstantName.C_SOURCE, length = SchemaConstantSize.SOURCE)
    private String source;

    @Column(name = SchemaColumnConstantName.C_VERSION, length = SchemaConstantSize.VERSION)
    private String version;

    @Builder.Default
    @ColumnDefault("'EDITING'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_STATUS, length = IEnumTemplateStatus.STR_ENUM_SIZE, nullable = false)
    private IEnumTemplateStatus.Types state = IEnumTemplateStatus.Types.EDITING;


    @Builder.Default
    @ColumnDefault("'PRV'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_VISIBILITY, length = IEnumTemplateVisibility.STR_ENUM_SIZE, nullable = false)
    private IEnumTemplateVisibility.Types visibility = IEnumTemplateVisibility.Types.PRV;

    @Builder.Default
    @ColumnDefault("'EN'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_LANGUAGE, length = IEnumLanguage.STR_ENUM_SIZE, nullable = false)
    private IEnumLanguage.Types language = IEnumLanguage.Types.EN;

    @ManyToOne
    @JoinColumn(name = SchemaColumnConstantName.C_AUHTOR, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_REF_AUTHOR))
    private Author author;

    @ManyToOne
    @JoinColumn(name = SchemaColumnConstantName.C_CATEGORY, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_REF_CATEGORY))
    private Category category;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_FAVORITE_TEMPLATE
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_TEMPLATE,
            referencedColumnName = SchemaColumnConstantName.C_CODE,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_FAVORITE_REF_TEMPLATE)))
    @Column(name = SchemaColumnConstantName.C_USER_NAME)
    private List<String> favorites;

    //BEGIN IFileEntity : SecondaryTable / TemplateFile
    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_TEMPLATE_FILE)
    private String originalFileName;
    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_TEMPLATE_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_TEMPLATE_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_TEMPLATE,
            referencedColumnName = SchemaColumnConstantName.C_CODE,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAG_REF_TEMPLATE)))
    @Column(name = SchemaColumnConstantName.C_TAG)
    private List<String> tags;
    //END IFileEntity : SecondaryTable
}
