package eu.isygoit.model;

import eu.isygoit.enums.IEnumSalaryType;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Salary information.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_CONTRACT_SALARY_INFORMATION)
public class SalaryInformation extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "salaryPackage_Info_sequence_generator", sequenceName = "salary_info_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salaryPackageInfo_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_SALARY_TYPE, length = IEnumSalaryType.STR_ENUM_SIZE)
    private IEnumSalaryType.Types salaryType;
    @Column(name = SchemaColumnConstantName.C_FREQUENCY)
    private Integer frequency;
    @Column(name = SchemaColumnConstantName.C_GROSS_SALARY)
    private Double grossSalary;
    @Column(name = SchemaColumnConstantName.C_NET_SALARY)
    private Double netSalary;
    @Column(name = SchemaColumnConstantName.C_CURRENCY, length = SchemaConstantSize.XL_VALUE)
    private String currency;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_SALARY_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SALARY_REF_PRIME))
    private List<Prime> primes;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_SALARY_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SALARY_REF_SHECUDLE_PAYMENT))
    private List<PaymentSchedule> paymentSchedules;

}
