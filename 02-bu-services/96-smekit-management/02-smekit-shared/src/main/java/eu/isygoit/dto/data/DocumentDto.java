package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumDocTempStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DocumentDto extends AbstractAuditableDto<Long> implements IFileUploadDto {
    private String domain;
    private String code;
    private String name;
    private String description;
    private LocalDateTime editionDate;
    private String content;
    private String dmsFileId;

    @Builder.Default
    private Boolean shared = Boolean.FALSE;

    @Builder.Default
    private Boolean isTemplateCopy = false;

    private Long originalDocumentId;

    @Builder.Default
    private IEnumDocTempStatus.Types tempType = IEnumDocTempStatus.Types.EDITING;

    private MultipartFile file;
    private String fileName;
    private String originalFileName;
    private String type;
    private String path;
    private String extension;

    @Builder.Default
    private Set<SharedWithDto> sharedWithUsers = new HashSet<>();

    @Builder.Default
    private Set<DocCommentDto> comments = new HashSet<>();

    private TemplateDto template;



}