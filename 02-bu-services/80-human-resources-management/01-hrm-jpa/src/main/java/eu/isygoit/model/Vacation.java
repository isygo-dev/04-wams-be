package eu.isygoit.model;

import eu.isygoit.enums.IEnumAbsenceType;
import eu.isygoit.enums.IEnumAbseneceStatus;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Vacation.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_VACATION)
public class Vacation extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "vacation_sequence_generator", sequenceName = "vacation_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacation_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Column(name = SchemaColumnConstantName.C_START_DATE, length = SchemaConstantSize.C_DATE)
    private String startDate;

    @Column(name = SchemaColumnConstantName.C_END_DATE, length = SchemaConstantSize.C_DATE)
    private String endDate;
    @Column(name = SchemaColumnConstantName.C_LEAVE_TAKEN)
    private Double leaveTaken;
    @Column(name = SchemaColumnConstantName.C_RECOVERY_LEAVE_TAKEN)
    private Double recoveryLeaveTaken;
    @Column(name = SchemaColumnConstantName.C_COMMENT, length = ComSchemaConstantSize.DESCRIPTION)
    private String comment;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_ABSENCE_TYPE, length = IEnumAbsenceType.STR_ENUM_SIZE)
    private IEnumAbsenceType.Types absence;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_STATUS, length = IEnumAbseneceStatus.STR_ENUM_SIZE)
    private IEnumAbseneceStatus.Types status;
}
