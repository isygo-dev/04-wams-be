package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Holiday information.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_CONTRACT_HOLIDAY_INFO)
public class HolidayInformation extends AuditableEntity<Long> {
    @Id
    @SequenceGenerator(name = "holiday_sequence_generator", sequenceName = "holiday_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "holiday_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Column(name = SchemaColumnConstantName.C_ANNUAL_LEGAL_LEAVE_COUNT)
    private Double legalLeaveCount;
    @Column(name = SchemaColumnConstantName.C_ANNUAL_FEEDING_FREQUENCY)
    private Integer feedingFrequency;
    @Column(name = SchemaColumnConstantName.C_ANNUAL_RECOVERY_LEAVE_COUNT)
    private Double recoveryLeaveCount;
    @Column(name = SchemaColumnConstantName.C_RECOVERY_FEEDING_FREQUENCY)
    private Integer recoveryFeedingFrequency;
}
