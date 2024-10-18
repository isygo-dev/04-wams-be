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

/**
 * The type Asso account employee.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_ASSO_ACCOUNT_EMPLOYEE)
public class AssoAccountEmployee extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "account_employee_sequence_generator", sequenceName = "account_employee_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_employee_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_ACCOUNT_CODE, length = ComSchemaConstantSize.CODE, nullable = false)
    private String accountCode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_EMPLOYEE, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_ACCOUNT_REF_EMPLOYEE))
    private Employee employee;
}
