package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

/**
 * The type Candidate quiz answer.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_CANDIDATE_QUIZ_ANSWER)
public class CandidateQuizAnswer extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "cand_quiz_answer_sequence_generator", sequenceName = "cand_quiz_answer_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cand_quiz_answer_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_QUESTION_ID, nullable = false)
    private Long question;

    @Column(name = SchemaColumnConstantName.C_OPTION_ID)
    private Long option;

    @Lob
    @Column(name = SchemaColumnConstantName.C_TEXT_ANSWER)
    private String answerText;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = SchemaColumnConstantName.C_START_DATE, updatable = false, nullable = false)
    private Date startDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = SchemaColumnConstantName.C_SUBMIT_DATE)
    private Date submitDate;
    @Builder.Default
    @ColumnDefault("'0'")
    @Column(name = SchemaColumnConstantName.C_SCORE)
    private Double score = 0D;
}
