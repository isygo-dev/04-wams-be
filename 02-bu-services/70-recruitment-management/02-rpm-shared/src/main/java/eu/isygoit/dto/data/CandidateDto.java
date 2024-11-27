package eu.isygoit.dto.data;


import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.enums.IEnumAccountOrigin;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Candidate dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CandidateDto extends IdentifiableDto<Long> {

    private String code;
    private String accountCode;
    @NotEmpty
    private String email;
    private String fullName;
    @Builder.Default
    private String origin = IEnumAccountOrigin.Types.SYS_ADMIN.name();
}
