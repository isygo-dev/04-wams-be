package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The type Post dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostDto extends AbstractAuditableDto<Long> implements IImageUploadDto, IFileUploadDto {

    @NotEmpty
    private String domain;
    @NotEmpty
    private String accountCode;
    @NotEmpty
    private String title;
    @NotEmpty
    private String talk;
    private List<PostCommentDto> comments;
    private List<String> usersAccountCode;
    private String imagePath;
    private String fileName;
    private String originalFileName;
    private String extension;
    private List<String> tags;
    private MultipartFile file;
    private String type;
    private List<String> usersAccountCodeDislike;
    private Boolean isBlog;
}
