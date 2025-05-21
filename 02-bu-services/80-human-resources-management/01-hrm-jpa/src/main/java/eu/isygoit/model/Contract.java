package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumContractType;
import eu.isygoit.enums.IEnumTimeType;
import eu.isygoit.enums.IEnumWorkMode;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Contract.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_CONTRACT, uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_CONTRACT_CODE,
                columnNames = {SchemaColumnConstantName.C_CODE})
})
@SecondaryTable(name = SchemaTableConstantName.T_CONTRACT_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID,
                referencedColumnName = SchemaColumnConstantName.C_ID)
)
@SQLDelete(sql = "update " + SchemaTableConstantName.T_CONTRACT + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class Contract extends AuditableCancelableEntity<Long>
        implements IDomainAssignable, ICodeAssignable, IFileEntity {

    @Id
    @SequenceGenerator(name = "contract_sequence_generator", sequenceName = "contract_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    //@Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;
    //@Convert(converter = LowerCaseConverter.class)
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    @Column(name = SchemaColumnConstantName.C_EMPLOYEE_ID, updatable = false)
    private Long employee;
    @Column(name = SchemaColumnConstantName.C_LOCATION, length = SchemaConstantSize.XL_VALUE)
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_WORKING_MODE, length = IEnumWorkMode.STR_ENUM_SIZE)
    private IEnumWorkMode.Types workingMode;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_CONTRACT_TYPE, length = IEnumContractType.STR_ENUM_SIZE)
    private IEnumContractType.Types contract;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_AVAILABILITY, length = IEnumTimeType.STR_ENUM_SIZE)
    private IEnumTimeType.Types availability;
    @Column(name = SchemaColumnConstantName.C_PROBATIONARY_PERIOD)
    private Integer probationaryPeriod;
    @Column(name = SchemaColumnConstantName.C_START_DATE)
    private LocalDate startDate;
    @Column(name = SchemaColumnConstantName.C_END_DATE)
    private LocalDate endDate;
    @Column(name = SchemaColumnConstantName.C_VACATION_BALANCE)
    private Integer vacationBalance;
    @Column(name = SchemaColumnConstantName.C_IS_RENEWABLE)
    private Boolean isRenewable;
    @Column(name = SchemaColumnConstantName.C_IS_LOCKED)
    private Boolean isLocked;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = SchemaColumnConstantName.C_SALARY_INFO, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CONTRACT_REF_SALARY_INFO))
    private SalaryInformation salaryInformation;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_HOLIDAY_INFO, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CONTRACT_REF_HOLIDAY_INFO))
    private HolidayInformation holidayInformation;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_CONTRACT_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CONTRACT_REF_AVANTAGE))
    private List<Advantage> advantages;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_CONTRACT_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CONTRACT_REF_EQUIPMENT))
    private List<Equipment> equipments;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_CONTRACT_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CONTRACT_REF_AMENDMENT))
    private List<ContractAmendment> contractAmendments;

    //BEGIN IFileEntity : SecondaryTable / ContractFile
    @Column(name = SchemaColumnConstantName.C_FILE_NAME, table = SchemaTableConstantName.T_CONTRACT_FILE)
    private String fileName;
    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_CONTRACT_FILE)
    private String originalFileName;
    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_PATH, table = SchemaTableConstantName.T_CONTRACT_FILE)
    private String path;
    @Column(name = SchemaColumnConstantName.C_EXTENSION, table = SchemaTableConstantName.T_CONTRACT_FILE)
    private String extension;
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_CONTRACT_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_CONTRACT_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_CONTRACT,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_CONTRACT_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
    //END IFileEntity : SecondaryTable
}
