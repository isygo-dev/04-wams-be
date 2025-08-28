package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AuditableDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Post comment dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostCommentDto extends AuditableDto<Long> {

    @NotEmpty
    private String accountCode;
    @NotEmpty
    private String text;
    @NotNull
    private Long postId;
    private List<String> usersAccountCode;
}
