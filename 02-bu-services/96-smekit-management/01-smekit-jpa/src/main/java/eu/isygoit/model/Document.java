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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = SchemaTableConstantName.T_DOCUMENT)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Document  extends AuditableEntity<Long> implements ISAASEntity {
    @Id
        @SequenceGenerator(
            name = "document_sequence_generator",
            sequenceName = "document_sequence",
            allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "document_sequence_generator")
        @Column(
            name = SchemaColumnConstantName.C_ID,
            updatable = false, nullable = false)
    private  Long id;

    @ColumnDefault(DomainConstants.DEFAULT_DOMAIN_NAME)
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private  String domain;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private  String name;

    @Column(
            name = SchemaColumnConstantName.C_DESCRIPTION,
            length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    private LocalDateTime editionDate;

    @Builder.Default
    @ColumnDefault("'false'")
    private Boolean shared = Boolean.FALSE;

    @Builder.Default
    @ColumnDefault("'EDITING'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.D_S, length = IEnumDocTempStatus.STR_ENUM_SIZE, nullable = false)
    private IEnumDocTempStatus.Types type = IEnumDocTempStatus.Types.EDITING;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name =SchemaColumnConstantName.C_SH_WTH , referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_DOCUMENTS_SHAREDWITH))
    private  Set<SharedWith> sharedWithUsers ;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name =SchemaColumnConstantName.C_COMMENT , referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_DOCUMENTS_COMMENTS))
    private  Set<DocComment> comments;

    @ManyToOne
    @JoinColumn(name = SchemaColumnConstantName.C_CAT, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_DOCUMENTS))
    private Template template;
}





