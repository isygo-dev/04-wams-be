package eu.isygoit.model;

import eu.isygoit.enums.IEnumInterviewSkillType;
import eu.isygoit.enums.IEnumSkillLevelType;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

/**
 * The type Interview skills.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_INTERVIEW_SKILLS)
public class InterviewSkills extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "interview_skills_sequence_generator", sequenceName = "interview_skills_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interview_skills_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_SKILL_TYPE, length = IEnumSkillLevelType.STR_ENUM_SIZE)
    private IEnumInterviewSkillType.Types type;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Builder.Default
    @ColumnDefault("'BEGINNER'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_LEVEL, length = IEnumSkillLevelType.STR_ENUM_SIZE, nullable = false)
    private IEnumSkillLevelType.Types level = IEnumSkillLevelType.Types.BEGINNER;

    @Builder.Default
    @ColumnDefault("'0'")
    @Column(name = SchemaColumnConstantName.C_SCORE)
    private Double score = 0D;
}
