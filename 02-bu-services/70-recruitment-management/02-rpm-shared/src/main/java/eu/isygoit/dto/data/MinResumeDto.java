package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Min resume dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MinResumeDto extends AuditableDto<Long> {

    private String code;
    private String Tenant;
    private String title;
    private String presentation;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String imagePath;
    private String source;
    private ResumeDetailsDto details;
    private List<ResumeShareInfoDto> resumeShareInfos;
    private String originalFileName;
}
