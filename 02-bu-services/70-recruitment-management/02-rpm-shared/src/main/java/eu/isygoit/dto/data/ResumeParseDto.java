package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Resume parse dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResumeParseDto extends AbstractAuditableDto<Long> implements IFileUploadDto {

    private String domain;
    private String code;
    private String fileName;
    private String originalFileName;
    private MultipartFile file;
}
