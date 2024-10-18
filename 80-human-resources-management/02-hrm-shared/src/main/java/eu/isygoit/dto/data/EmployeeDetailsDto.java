package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumCivility;
import eu.isygoit.enums.IEnumGender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


/**
 * The type Employee details dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeDetailsDto extends AbstractAuditableDto<Long> {

    private String placeofBirth;
    private LocalDate birthDate;
    private List<PersonalIdentityInfoDto> cin;
    private String location;
    private String reportTo;
    private List<TravelIdentityInfoDto> passport;
    private List<InsuranceIdentityInfoDto> securities;
    private String securite;
    private String homeAddress;
    private IEnumGender.Types gender;
    private IEnumCivility.Types civility;
    @Nullable
    private FamilyInformationDto familyInformation;
    @Nullable
    private Set<EmergencyContactDto> emergencyContact;
    @Nullable
    private Set<EmployeeLanguagesDto> languages;

}
