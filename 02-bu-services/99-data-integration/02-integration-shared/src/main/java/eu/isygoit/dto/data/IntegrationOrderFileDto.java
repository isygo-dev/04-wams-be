package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The type Integration order file dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class IntegrationOrderFileDto extends IntegrationOrderDto implements IFileUploadDto {

    private String fileName;
    private String originalFileName;
    private String extension;
    private String type;
    private List<String> tags;
    private MultipartFile file;
}
