package eu.isygoit.model;

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
 * The type Quiz option.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_QUIZ_OPTION)
public class QuizOption extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "quiz_option_sequence_generator", sequenceName = "quiz_option_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_option_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_OPTION_TEXT, length = SchemaConstantSize.S_OPTION, nullable = false)
    private String option;

    @Builder.Default
    @ColumnDefault("'false'")
    @Column(name = SchemaColumnConstantName.C_QUIZ_OPTION_CHECK, nullable = false)
    private Boolean checked = Boolean.FALSE;

    @Column(name = SchemaColumnConstantName.C_TEXT_ANSWER, length = ComSchemaConstantSize.XXL_DESCRIPTION)
    private String textAnswer;
}
