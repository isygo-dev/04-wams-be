package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
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
 * The type Personal identity info.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_PERSONAL_IDENTITY_INFO)
public class PersonalIdentityInfo extends AuditableEntity<Long> implements IImageEntity, ISAASEntity, ICodifiable {

    @Id
    @SequenceGenerator(name = "personal_identity_sequence_generator", sequenceName = "personal_identity_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personal_identity_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Column(name = SchemaColumnConstantName.C_CIN_NUMBER, length = ComSchemaConstantSize.CIN)
    private String cardNumber;
    @Column(name = SchemaColumnConstantName.C_ISSUED_DATE)
    private LocalDate issuedDate;
    @Column(name = SchemaColumnConstantName.C_CITY_OF_DELIVERY, length = ComSchemaConstantSize.CIN)
    private String issuedPlace;
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