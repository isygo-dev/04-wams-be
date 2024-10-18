package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * The type Family information.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_EMPLOYEE_FAMILY_INFORMATION)
public class FamilyInformation extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "familyInfo_sequence_generator", sequenceName = "family_info_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "familyInfo_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Column(name = SchemaColumnConstantName.C_SPOUSE_NAME, length = ComSchemaConstantSize.S_NAME)
    private String spouseName;
    @Column(name = SchemaColumnConstantName.C_NUMBER_CHILDREN)
    private Integer numberOfChildren;
    @ElementCollection
    @CollectionTable(
            name = SchemaTableConstantName.T_CHILDREN_INFORMATION,
            joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_ID, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_FAMILY_REF_CHILDREN_INFORMATION))
    )
    private Set<ChildrenInformation> childrenInformations;


}
