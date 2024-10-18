package eu.isygoit.model;

import eu.isygoit.enums.IEnumCivility;
import eu.isygoit.enums.IEnumGender;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaFkConstantName;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * The type Employee details.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_EMPLOYEE_DETAILS)
public class EmployeeDetails extends AuditableEntity<Long> {

    @Id
    @SequenceGenerator(name = "employee_details_sequence_generator", sequenceName = "employee_details_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_details_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_GENDER)
    private IEnumGender.Types gender;
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_CIVILITY, nullable = false)
    private IEnumCivility.Types civility;
    @Column(name = SchemaColumnConstantName.C_BIRTHPLACE, length = ComSchemaConstantSize.BIRTHPLACE)
    private String placeofBirth;
    @Column(name = SchemaColumnConstantName.C_REPORT_TO, length = ComSchemaConstantSize.REPORT_TO)
    private String reportTo;
    @Column(name = SchemaColumnConstantName.C_LOCATION, length = ComSchemaConstantSize.LOCATION)
    private String location;
    @Column(name = SchemaColumnConstantName.C_BIRTHDAY)
    private LocalDate birthDate;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_EMPLOYEE_DETAILS_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_CIN_REF_EMPLOYEE))
    private List<PersonalIdentityInfo> personalIdentityInfo;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_EMPLOYEE_DETAILS_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_PASSPORT_REF_EMPLOYEE))
    private List<TravelIdentityInfo> travelIdentityInfo;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_EMPLOYEE_DETAILS_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SECURITY_REF_EMPLOYEE))
    private List<InsuranceIdentityInfo> securities;
    @ElementCollection
    @CollectionTable(
            name = SchemaTableConstantName.T_EMPLOYEE_LANGUAGE,
            joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_ID, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_EMPLOYEE_REF_LANG))
    )
    private Set<EmployeeLanguages> languages;

    @ElementCollection
    @CollectionTable(
            name = SchemaTableConstantName.T_EMERGENCY_CONTACT,
            joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_ID, foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_EMPLOYEE_REF_EMERGENCY_CONTACT))
    )
    private Set<EmergencyContact> emergencyContact;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_FAMILY_INFORMATION, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_EMPLOYEE_REF_FAMILY_INFOR))
    private FamilyInformation familyInformation;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_PROFESSIONAL_INFORMATION, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_EMPLOYEE_REF_PROF_INFO))
    private ProfessionalInformation professionalInformation;


}
