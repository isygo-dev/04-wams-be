package eu.isygoit.dto.request;

import eu.isygoit.dto.extendable.AccountModelDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Share resume request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ShareResumeRequestDto {

    private String resumeOwner;
    private List<AccountModelDto> accountsCode;
}
