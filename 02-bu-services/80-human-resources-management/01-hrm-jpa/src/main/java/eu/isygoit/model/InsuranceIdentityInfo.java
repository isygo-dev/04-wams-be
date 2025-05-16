package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumInsuranceType;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

/**
 * The type Insurance identity info.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_INSURANCE_IDENTITY_INFO)
public class InsuranceIdentityInfo extends AuditableEntity<Long> implements IImageEntity, IDomainAssignable, ICodeAssignable {

    @Id
    @SequenceGenerator(name = "insurance_identity_sequence_generator", sequenceName = "insurance_identity_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insurance_identity_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Column(name = SchemaColumnConstantName.C_SECURITY_NUMBER, length = ComSchemaConstantSize.S_NUMBER)
    private String cardNumber;
    @Column(name = SchemaColumnConstantName.C_ISSUED_DATE)
    private LocalDate issuedDate;
    @Column(name = SchemaColumnConstantName.C_EXPIRED_DATE)
    private LocalDate expiredDate;
    @Column(name = SchemaColumnConstantName.C_CITY_OF_DELIVERY, length = SchemaConstantSize.ISSUED_PLACE)
    private String issuedPlace;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_INSURANCE_TYPE, length = IEnumInsuranceType.STR_ENUM_SIZE)
    private IEnumInsuranceType.Types insuranceType;
    @Column(name = SchemaColumnConstantName.C_PHOTO)
    private String imagePath;
    //@Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;
    //@Convert(converter = LowerCaseConverter.class)
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    @Column(name = SchemaColumnConstantName.C_EMPLOYEE_DETAILS_ID, updatable = false)
    private Long employeeDetailsId;
}
