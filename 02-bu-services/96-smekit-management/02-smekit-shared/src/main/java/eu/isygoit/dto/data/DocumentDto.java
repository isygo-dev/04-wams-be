package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumDocTempStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DocumentDto extends AbstractAuditableDto<Long> {
    private  String domain;
    private  String name;
    private String description;
    private LocalDateTime editionDate;
    private Boolean shared = Boolean.FALSE;
    private IEnumDocTempStatus.Types type = IEnumDocTempStatus.Types.EDITING;
   // private  Set<SharedWithDto> sharedWith ;
    // private  Set<DocCommentDto> comments;
    private TemplateDto template;



}
