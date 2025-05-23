package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumDocCommentsStaus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class DocCommentDto extends AbstractAuditableDto<Long> {
    private String text;
    private String user;
    private IEnumDocCommentsStaus.Types type = IEnumDocCommentsStaus.Types.CLOSED;
    private DocumentDto document;

}
