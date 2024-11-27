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

import java.util.UUID;


/**
 * The type Blog talk.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(SchemaTableConstantName.T_BLOG_TALK)
public class BlogTalk extends AuditableEntity<UUID> {

    @PrimaryKey
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID id;
    @CassandraType(type = CassandraType.Name.BIGINT)
    @Column(SchemaColumnConstantName.C_BLOG_ID)
    private Long blogId;
    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_TALK)
    private String text;
    @CassandraType(type = CassandraType.Name.UUID)
    @Column(SchemaColumnConstantName.C_PARENT)
    private UUID parent;
    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(SchemaColumnConstantName.C_ACCOUNT)
    private String accountCode;
}
