package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import eu.isygoit.enums.IEnumCommentsStaus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DocumentCommentDto extends AuditableDto<Long> {

    private String text;
    private String user;
    @Builder.Default
    private IEnumCommentsStaus.Types status = IEnumCommentsStaus.Types.CLOSED;
}
