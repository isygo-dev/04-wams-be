package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
                columnNames = {SchemaColumnConstantName.C_ACCOUNT, SchemaColumnConstantName.C_QUIZ})
})
public class CandidateQuiz extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "candidate_quiz_sequence_generator", sequenceName = "candidate_quiz_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidate_quiz_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_ACCOUNT, length = SchemaConstantSize.CODE, updatable = false, nullable = false)
    private String accountCode;

    @Column(name = SchemaColumnConstantName.C_QUIZ, length = SchemaConstantSize.CODE, updatable = false, nullable = false)
    private String quizCode;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = SchemaColumnConstantName.C_START_DATE, updatable = false, nullable = false)
    private Date startDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = SchemaColumnConstantName.C_SUBMIT_DATE)
    private Date submitDate;

    @Column(name = SchemaColumnConstantName.C_SCORE)
    private Long score;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_CAND_QUIZ, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CAND_QUIZ_ANS_REF_CAND_QUIZ))
    private List<CandidateQuizAnswer> answers;
}
