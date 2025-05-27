package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumLanguage;
import eu.isygoit.enums.IEnumTemplateStatus;
import eu.isygoit.enums.IEnumTemplateVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TemplateDto extends AbstractAuditableDto<Long> implements IFileUploadDto {

    private String domain;
    private String name;
    private String description;
    private String path;
    private String fileName;
    private String extension;
    private String source;
    private String version;
    private IEnumTemplateStatus.Types status;
    private IEnumTemplateVisibility.Types visibility;
    private IEnumLanguage.Types language;

    private Long authorId;
    private Long categoryId;

    private String type;
    private MultipartFile file;
    private String originalFileName;


}
