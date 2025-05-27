package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumTemplateStatus;
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
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = SchemaTableConstantName.T_DOCUMENT)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Document extends AuditableEntity<Long> implements IDomainAssignable, ICodeAssignable, IStatusAssignable<IEnumTemplateStatus.Types> {

    @Id
    @SequenceGenerator(name = "document_sequence_generator", sequenceName = "document_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String domain;

    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = true, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = SchemaConstantSize.DESCRIPTION)
    private String description;

    @Builder.Default
    @ColumnDefault("'EDITING'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_STATUS, length = IEnumTemplateStatus.STR_ENUM_SIZE, nullable = false)
    private IEnumTemplateStatus.Types state = IEnumTemplateStatus.Types.EDITING;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_SHARED_WITH
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_DOCUMENT,
            referencedColumnName = SchemaColumnConstantName.C_CODE,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SHARED_WITH_REF_DOCUMENT)))
    @Column(name = SchemaColumnConstantName.C_USER_NAME)
    private List<String> sharedWithUsers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_COMMENT_ID, referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_COMMENT_REF_DOCUMENT))
    private Set<DocumentComment> comments;

    @ManyToOne(fetch = FetchType.LAZY /* NO CASCADE */)
    @JoinColumn(name = SchemaColumnConstantName.C_TEMPLATE, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_DOCUMENT_REF_TEMPLATE))
    private Template template;
}





