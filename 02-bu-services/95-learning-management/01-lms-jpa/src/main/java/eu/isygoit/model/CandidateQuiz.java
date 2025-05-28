package eu.isygoit.model;

import eu.isygoit.converter.LowerCaseConverter;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;
import java.util.List;

/**
 * The type Candidate quiz.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_CANDIDATE_QUIZ
        , uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_CANDIDATE_QUIZ_CODE,
                columnNames = {SchemaColumnConstantName.C_CODE})
})
@SQLDelete(sql = "update " + SchemaTableConstantName.T_CANDIDATE_QUIZ + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class CandidateQuiz extends AuditableCancelableEntity<Long> implements ICodeAssignable {

    @Id
    @SequenceGenerator(name = "candidate_quiz_sequence_generator", sequenceName = "candidate_quiz_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_quiz_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Convert(converter = LowerCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, unique = true, updatable = false, nullable = false)
    private String code;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = SchemaColumnConstantName.C_START_DATE, updatable = false)
    private Date startDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = SchemaColumnConstantName.C_SUBMIT_DATE)
    private Date submitDate;

    @Column(name = SchemaColumnConstantName.C_QUIZ, length = SchemaConstantSize.CODE, updatable = false, nullable = false)
    private String quizCode;

    @Column(name = SchemaColumnConstantName.C_SCORE)
    private Long score;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_CAND_QUIZ, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CAND_QUIZ_ANS_REF_CAND_QUIZ))
    private List<CandidateQuizAnswer> answers;
}
