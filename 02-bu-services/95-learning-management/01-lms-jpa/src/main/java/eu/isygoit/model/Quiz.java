package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

/**
 * The type Quiz.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_QUIZ
        , uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_QUIZ_CODE,
                columnNames = {SchemaColumnConstantName.C_CODE})
})
@SQLDelete(sql = "update " + SchemaTableConstantName.T_QUIZ + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class Quiz extends AuditableCancelableEntity<Long> implements ICodifiable {

    @Id
    @SequenceGenerator(name = "quiz_sequence_generator", sequenceName = "quiz_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    //@Convert(converter = LowerCaseConverter.class)
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    @Column(name = SchemaColumnConstantName.C_CATEGORY, length = SchemaConstantSize.CATEGORY, nullable = false)
    private String category;

    @OrderBy(ComSchemaColumnConstantName.C_RANK + " DESC")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_QUIZ, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SECTION_REF_QUIZ))
    private List<QuizSection> sections;
}
