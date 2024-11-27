package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Quiz section.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_QUIZ_SECTION)
public class QuizSection extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "quiz_section_sequence_generator", sequenceName = "quiz_section_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_section_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = ComSchemaConstantSize.DESCRIPTION)
    private String description;

    @Column(name = SchemaColumnConstantName.C_RANK)
    private Integer order;

    @OrderBy(ComSchemaColumnConstantName.C_RANK + " DESC")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_QUIZ_SECTION, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SECTION_REF_QUIZ))
    private List<QuizQuestion> questions;
}
