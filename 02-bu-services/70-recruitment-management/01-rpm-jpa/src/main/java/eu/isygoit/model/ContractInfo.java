package eu.isygoit.model;

import eu.isygoit.enums.IEnumContractType;
import eu.isygoit.enums.IEnumTimeType;
import eu.isygoit.enums.IEnumWorkMode;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Contract info.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER_CONTRACT_INFO)
public class ContractInfo extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "resume_details_sequence_generator", sequenceName = "resume_details_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_details_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = SchemaColumnConstantName.C_LOCATION, length = SchemaConstantSize.XL_VALUE)
    private String location;
    @Column(name = SchemaColumnConstantName.C_SALARY_MIN)
    private Integer salaryMin;
    @Column(name = SchemaColumnConstantName.C_SALARY_MAX)
    private Integer salaryMax;
    @Column(name = SchemaColumnConstantName.C_CURRENCY, length = SchemaConstantSize.XL_VALUE)
    private String currency;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_WORKING_MODE, length = IEnumWorkMode.STR_ENUM_SIZE)
    private IEnumWorkMode.Types workingMode;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_CONTRACT_TYPE, length = IEnumContractType.STR_ENUM_SIZE)
    private IEnumContractType.Types contract;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_AVAILABILITY, length = IEnumTimeType.STR_ENUM_SIZE)
    private IEnumTimeType.Types availability;
}
