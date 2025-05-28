package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.converter.LowerCaseConverter;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

/**
 * The type Integration flow.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_INTEGRATION_FLOW
        , uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_INTEGRATION_FLOW_CODE, columnNames = {SchemaColumnConstantName.C_CODE})
})
@SecondaryTable(name = SchemaTableConstantName.T_INTEGRATION_FLOW_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID,
                referencedColumnName = SchemaColumnConstantName.C_ID)
)
public class IntegrationFlow extends AuditableEntity<Long> implements ICodeAssignable, IDomainAssignable, IFileEntity {

    @Id
    @SequenceGenerator(name = "integration_flow_sequence_generator", sequenceName = "integration_flow_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "integration_flow_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;

    @Convert(converter = LowerCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, unique = true, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_ORDER_NAME, length = ComSchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String orderName;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = SchemaColumnConstantName.C_INTEGRATION_DATE, updatable = false)
    private Date integrationDate;

    //BEGIN IFileEntity : SecondaryTable / IntegrationFlowFile
    @Column(name = SchemaColumnConstantName.C_FILE_NAME, table = SchemaTableConstantName.T_INTEGRATION_FLOW_FILE)
    private String fileName;
    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_INTEGRATION_FLOW_FILE)
    private String originalFileName;
    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_PATH, table = SchemaTableConstantName.T_INTEGRATION_FLOW_FILE)
    private String path;
    @Column(name = SchemaColumnConstantName.C_EXTENSION, table = SchemaTableConstantName.T_INTEGRATION_FLOW_FILE)
    private String extension;
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_INTEGRATION_FLOW_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_INTEGRATION_FLOW_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_INTEGRATION_FILE,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAG_REF_INTEGRATION_FLOW_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
    //END IFileEntity : SecondaryTable
}
