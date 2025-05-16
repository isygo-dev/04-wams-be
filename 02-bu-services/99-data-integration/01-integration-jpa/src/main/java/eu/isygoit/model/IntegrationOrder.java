package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumIntegrationOrder;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

/**
 * The type Integration order.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_INTEGRATION_ORDER
        , uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_INTEGRATION_ORDER_CODE,
                columnNames = {SchemaColumnConstantName.C_CODE}),
        @UniqueConstraint(name = SchemaUcConstantName.UC_INTEGRATION_ORDER_DOMAIN_NAME,
                columnNames = {SchemaColumnConstantName.C_DOMAIN, SchemaColumnConstantName.C_NAME})
})
@SecondaryTable(name = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID,
                referencedColumnName = SchemaColumnConstantName.C_ID)
)
public class IntegrationOrder extends AuditableEntity<Long> implements ICodeAssignable, IDomainAssignable, IFileEntity {

    @Id
    @SequenceGenerator(name = "integration_api_sequence_generator", sequenceName = "integration_api_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "integration_api_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    //@Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;

    //@Convert(converter = LowerCaseConverter.class)
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    @Column(name = SchemaColumnConstantName.C_SERVICE, length = ComSchemaConstantSize.DESCRIPTION)
    private String serviceName;

    @Column(name = SchemaColumnConstantName.C_MAPPING, length = ComSchemaConstantSize.DESCRIPTION)
    private String mapping;

    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_INTEGRATION_ORDER, length = IEnumIntegrationOrder.STR_ENUM_SIZE, nullable = false)
    private IEnumIntegrationOrder.Types integrationOrder;

    //BEGIN IFileEntity : SecondaryTable / IntegrationOrderValidationFile
    @Column(name = SchemaColumnConstantName.C_FILE_NAME, table = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE)
    private String fileName;
    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE)
    private String originalFileName;
    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_PATH, table = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE)
    private String path;
    @Column(name = SchemaColumnConstantName.C_EXTENSION, table = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE)
    private String extension;
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_INTEGRATION_ORDER_VALIDATION_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_INTEGRATION_FILE,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_INTEGRATION_ORDER_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
    //END IFileEntity : SecondaryTable
}
