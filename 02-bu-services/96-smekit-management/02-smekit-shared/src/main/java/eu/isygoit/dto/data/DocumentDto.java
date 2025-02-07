package eu.isygoit.dto.data;

import eu.isygoit.enums.IEnumDocTempStatus;

import java.time.LocalDateTime;
import java.util.Set;

public class DocumentDto {
    private  String domain;
    private  String name;
    private String description;
    private LocalDateTime editionDate;
    private Boolean shared = Boolean.FALSE;
    private IEnumDocTempStatus.Types type = IEnumDocTempStatus.Types.EDITING;
    private  Set<SharedWithDto> sharedWithUsers ;
    private  Set<DocCommentDto> comments;
    private TemplateDto template;



}
