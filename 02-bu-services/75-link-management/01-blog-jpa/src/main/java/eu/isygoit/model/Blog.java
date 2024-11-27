package eu.isygoit.model;


import eu.isygoit.model.cassandra.AuditableEntity;
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

/**
 * The type Blog.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(SchemaTableConstantName.T_BlOG)
public class Blog extends AuditableEntity<Long> {

    @PrimaryKey
    @CassandraType(type = CassandraType.Name.BIGINT)
    private Long id;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_TITLE)
    private String title;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_DOMAIN)
    private String domain;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_DESCRIPTION)
    private String description;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_ACCOUNT)
    private String accountCode;
}
