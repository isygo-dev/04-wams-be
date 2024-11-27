package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * The type Leave summary.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_LEAVE_STATUS, uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_CODE_YEAR, columnNames = {SchemaColumnConstantName.C_EMPLOYEE_CODE, SchemaColumnConstantName.C_YEAR})
})
public class LeaveSummary extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "leave_sequence_generator", sequenceName = "leave_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Column(name = SchemaColumnConstantName.C_EMPLOYEE_CODE, length = SchemaConstantSize.CODE, updatable = false, nullable = false)
    private String codeEmployee;
    @Column(name = SchemaColumnConstantName.C_EMPLOYEE_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String nameEmployee;
    @Column(name = SchemaColumnConstantName.C_ANNUAL_LEGAL_LEAVE_COUNT)
    private Double leaveCount;
    @Column(name = SchemaColumnConstantName.C_ANNUAL_LEGAL_LEAVE_TAKEN)
    private Double leaveTakenCount;
    @Column(name = SchemaColumnConstantName.C_ANNUAL_RECOVERY_LEAVE_COUNT)
    private Double recoveryLeaveCount;
    @Column(name = SchemaColumnConstantName.C_ANNUAL_RECOVERY_LEAVE_TAKEN)
    private Double recoveryLeaveTaken;
    @Column(name = SchemaColumnConstantName.C_YEAR, length = SchemaConstantSize.S_YEAR, updatable = false, nullable = false)
    private String year;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_LEAVE_SUMMARY_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_LEAVE_SUMMARY_REF_VACATION))
    private Set<Vacation> vacation;

    /**
     * Gets remaining leave count.
     *
     * @return the remaining leave count
     */
    public Double getRemainingLeaveCount() {
        if (leaveCount != null && leaveTakenCount != null) {
            return leaveCount - leaveTakenCount;
        } else {
            return null;
        }
    }


}
