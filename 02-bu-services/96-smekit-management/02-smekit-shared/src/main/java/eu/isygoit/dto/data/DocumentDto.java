package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumTemplateStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DocumentDto extends AbstractAuditableDto<Long> {
    private String domain;
    private String code;
    private String name;
    private String description;
    private LocalDateTime editionDate;
    private Boolean shared = Boolean.FALSE;
    private IEnumTemplateStatus.Types type = IEnumTemplateStatus.Types.EDITING;
    // private  Set<SharedWithDto> sharedWith ;
    // private  Set<DocCommentDto> comments;
    private TemplateDto template;


}
