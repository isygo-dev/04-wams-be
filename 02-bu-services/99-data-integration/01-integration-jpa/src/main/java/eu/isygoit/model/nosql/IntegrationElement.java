package eu.isygoit.model.nosql;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

/**
 * The type Integration element.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(SchemaTableConstantName.T_INTEGRATION_ELEMENT)
public class IntegrationElement extends AuditableEntity<UUID> {

    @PrimaryKey
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID id;

    @CassandraType(type = CassandraType.Name.BIGINT)
    @Column(SchemaColumnConstantName.C_FLOW_ID)
    private Long flowId;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_CONTENT)
    private String content;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_REF_ID)
    private String refId;
}
