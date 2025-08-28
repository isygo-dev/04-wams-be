package eu.isygoit.model;

import eu.isygoit.constants.TenantConstants;
import eu.isygoit.converter.LowerCaseConverter;
import eu.isygoit.enums.IEnumActionEvent;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

/**
 * The type Timeline.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_TIMELINE)
public class Timeline extends AuditableEntity<Long> implements ITenantAssignable, ICodeAssignable {

    @Id
    @SequenceGenerator(name = "timeline_sequence_generator", sequenceName = "timeline_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_details_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + TenantConstants.DEFAULT_TENANT_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_TENANT, length = SchemaConstantSize.TENANT, updatable = false, nullable = false)
    private String tenant;

    @Convert(converter = LowerCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, unique = true, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_PARENT_CODE, length = SchemaConstantSize.PARENT_CODE, updatable = false)
    private String parentCode;
    @Column(name = SchemaColumnConstantName.C_OBJECT, length = SchemaConstantSize.OBJECT, updatable = false, nullable = false)
    private String object;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_ACTION_CODE, length = IEnumActionEvent.STR_ENUM_SIZE, updatable = false, nullable = false)
    private IEnumActionEvent.Types action;
}
