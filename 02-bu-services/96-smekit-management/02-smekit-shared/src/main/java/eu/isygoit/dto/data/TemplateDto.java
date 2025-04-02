package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumDocTempStatus;
import eu.isygoit.enums.IEnumTemplateLanguage;
import eu.isygoit.enums.IEnumTemplateVisibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TemplateDto  extends AbstractAuditableDto<Long> implements IFileUploadDto {
    private  String domain;
    private String name;
    private String description;
    private String path ;
    private String fileName ;
    private String extension;
    private LocalDateTime editionDate;
    private String source;
    private String version;
    private IEnumDocTempStatus.Types typeTs ;
    private IEnumTemplateVisibility.Types typeTv ;
    private IEnumTemplateLanguage.Types typeTl ;
  //  private List<DocumentDto> documents;
    private Long authorId;
    private Long categoryId;

  //  private List<TagDto> tags ;
    //BEGIN IFileEntity : SecondaryTable / ResumeFile
    private String type;
  //  private List<String> TemplatesTags;
    //END IFileEntity : SecondaryTable
    private MultipartFile file;
    private String originalFileName;



}
