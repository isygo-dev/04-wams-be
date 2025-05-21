package eu.isygoit.model;

import eu.isygoit.constants.AccountTypeConstants;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumEnabledBinaryStatus;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Email;
import java.util.List;

/**
 * The type Employee.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_EMPLOYEE
        , uniqueConstraints = {
        @UniqueConstraint(name = SchemaUcConstantName.UC_EMPLOYEE_CODE,
                columnNames = {SchemaColumnConstantName.C_CODE})
})
@SQLDelete(sql = "update " + SchemaTableConstantName.T_EMPLOYEE + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class Employee extends AuditableCancelableEntity<Long> implements IDomainAssignable, ICodeAssignable, IImageEntity, IMultiFileEntity<EmployeeLinkedFile> {

    @Id
    @SequenceGenerator(name = "employee_sequence_generator", sequenceName = "employee_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    //@Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;
    //@Convert(converter = LowerCaseConverter.class)
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    //@Convert(converter = CamelCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_FIRST_NAME, length = SchemaConstantSize.S_NAME, nullable = false)
    private String firstName;
    //@Convert(converter = CamelCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_LAST_NAME, length = SchemaConstantSize.S_NAME, nullable = false)
    private String lastName;
    @Column(name = SchemaColumnConstantName.C_TITLE, length = ComSchemaConstantSize.S_NAME)
    private String title;
    @Column(name = SchemaColumnConstantName.C_EMAIL, length = SchemaConstantSize.EMAIL, nullable = false)
    @Email(message = "email.should.be.valid")
    private String email;
    @Column(name = SchemaColumnConstantName.C_PHONE_NUMBER, length = SchemaConstantSize.PHONE_NUMBER, nullable = false)
    private String phone;
    @Column(name = SchemaColumnConstantName.C_FUNCTION_ROLE, length = SchemaConstantSize.S_NAME, nullable = false)
    @ColumnDefault("'" + AccountTypeConstants.DOMAIN_USER + "'")
    private String functionRole = AccountTypeConstants.DOMAIN_USER;
    @Column(name = SchemaColumnConstantName.C_PHOTO)
    private String imagePath;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_ADDRESS, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_EMPLOYEE_REF_ADDRESS))
    private EmployeeAddress address;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_EMPLOYEE_DETAILS, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_EMPLOYEE_REF_DETAILS))
    private EmployeeDetails details;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ENABLED'")
    @Column(name = SchemaColumnConstantName.C_EMPLOYEE_STATUS, length = IEnumEnabledBinaryStatus.STR_ENUM_SIZE, nullable = false)
    private IEnumEnabledBinaryStatus.Types employeeStatus = IEnumEnabledBinaryStatus.Types.ENABLED;
    @Builder.Default
    @ColumnDefault("'true'")
    @Column(name = SchemaColumnConstantName.C_IS_LINKED_USER)
    private Boolean isLinkedToUser = Boolean.TRUE;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_EMPLOYEE_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_EMPLOYEE_REF_CONTRACT))
    private List<Contract> contracts;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_EMPLOYEE, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_ADDITIONAL_FILE_REF_EMPLOYEE))
    private List<EmployeeLinkedFile> additionalFiles;

    /**
     * Gets number active contracts.
     *
     * @return the number active contracts
     */
    public Integer getNumberActiveContracts() {
        if (!CollectionUtils.isEmpty(contracts)) {
            return contracts.size();
        } else {
            return 0;
        }
    }
}
