package eu.isygoit.model;

import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * The type Payment schedule.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_CONTRACT_PAYMENT_SHECUDLE)
public class PaymentSchedule extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "payment_schedule_sequence_generator", sequenceName = "payment_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_schedule_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Column(name = SchemaColumnConstantName.C_IS_SUBMITED)
    private Boolean isSubmited;
    @Column(name = SchemaColumnConstantName.C_SUBMIT_DATE)
    private LocalDate submitDate;
    @Column(name = SchemaColumnConstantName.C_DUE_DATE)
    private LocalDate dueDate;
    @Column(name = SchemaColumnConstantName.C_PAYMENT_GROSS_AMOUNT)
    private Double paymentGrossAmount;
    @Column(name = SchemaColumnConstantName.C_PAYMENT_NET_AMOUNT)
    private Double paymentNetAmount;

}
