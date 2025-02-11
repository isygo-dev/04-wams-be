package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumDocCommentsStaus;

public class DocCommentDto extends AbstractAuditableDto<Long> {
    private  String text;
    private  String user;
    private IEnumDocCommentsStaus.Types type = IEnumDocCommentsStaus.Types.CLOSED;
    private  DocumentDto document;

}
