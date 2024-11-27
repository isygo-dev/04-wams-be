package eu.isygoit.model;

import eu.isygoit.enums.IEnumSkillLevelType;
import eu.isygoit.enums.IEnumSkillType;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
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
 * The type Job offer skills.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER_SKILLS)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = SchemaColumnConstantName.C_TYPE, discriminatorType = DiscriminatorType.STRING)
public class JobOfferSkills extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "job_offer_skills_sequence_generator", sequenceName = "job_offer_skills_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_offer_skills_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'SOFT'")
    @Column(name = SchemaColumnConstantName.C_TYPE, length = IEnumSkillType.STR_ENUM_SIZE, insertable = false, updatable = false, nullable = false)
    private IEnumSkillType.Types type;
    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Builder.Default
    @ColumnDefault("'BEGINNER'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_LEVEL, length = IEnumSkillLevelType.STR_ENUM_SIZE, nullable = false)
    private IEnumSkillLevelType.Types level = IEnumSkillLevelType.Types.BEGINNER;
    @Builder.Default
    @ColumnDefault("'true'")
    @Column(name = ComSchemaConstantSize.C_IS_MANDATORY, nullable = false)
    private Boolean isMandatory = Boolean.TRUE;
}
