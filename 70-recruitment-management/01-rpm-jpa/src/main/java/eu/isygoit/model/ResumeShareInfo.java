package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Resume share info.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_RESUME_SHARE_INFO)
public class ResumeShareInfo extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "resume_share_info_sequence_generator", sequenceName = "resume_share_info_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_share_info_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_SHARED_WITH, nullable = false)
    private String sharedWith;
    @Column(name = SchemaColumnConstantName.C_RATING)
    private Integer rate;
    @Column(name = SchemaColumnConstantName.C_COMMENT, length = ComSchemaConstantSize.COMMENT)
    private String comment;
}
