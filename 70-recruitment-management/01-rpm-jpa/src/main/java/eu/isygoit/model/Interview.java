package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Interview.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_INTERVIEW)
public class Interview extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "interview_sequence_generator", sequenceName = "interview_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interview_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/, orphanRemoval = true)
    @JoinColumn(name = SchemaColumnConstantName.C_INTERVIEW, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SKILLS_REF_INTERVIEW))
    private List<InterviewSkills> skills;

    @Column(name = SchemaColumnConstantName.C_QUIZ, length = SchemaConstantSize.CODE)
    private String quizCode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_JOB_APPLICATION_EVENT, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_JOB_APP_EVENT_REF_INTERVIEW))
    private JobOfferApplicationEvent jobApplicationEvent;
}
