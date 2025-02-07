package eu.isygoit.dto.data;

import eu.isygoit.enums.IEnumDocCommentsStaus;

public class DocCommentDto {
    private  String text;
    private  String user;
    private IEnumDocCommentsStaus.Types type = IEnumDocCommentsStaus.Types.CLOSED;
    private  DocumentDto document;

}
