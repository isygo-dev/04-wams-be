package eu.isygoit.model;

import eu.isygoit.enums.IEnumCommentsStaus;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_DOCCOMMENT)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SQLDelete(sql = "update " + SchemaTableConstantName.T_DOCCOMMENT + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + SchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class DocumentComment extends AuditableCancelableEntity<Long> implements IStatusAssignable<IEnumCommentsStaus.Types> {

    @Id
    @SequenceGenerator(name = "doccomment_sequence_generator", sequenceName = "doccomment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doccomment_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_TEXT, length = SchemaConstantSize.COMMENT)
    private String text;

    @Column(name = SchemaColumnConstantName.C_USER_NAME, length = SchemaConstantSize.S_NAME)
    private String user;

    @Builder.Default
    @ColumnDefault("'CLOSED'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_STATUS, length = IEnumCommentsStaus.STR_ENUM_SIZE, nullable = false)
    private IEnumCommentsStaus.Types state = IEnumCommentsStaus.Types.CLOSED;
}






