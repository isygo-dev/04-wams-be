package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Resume dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResumeDto extends AbstractAuditableDto<Long> implements IFileUploadDto, IImageUploadDto {

    private String domain;
    private String code;
    private String firstName;
    private String lastName;
    private String nationality;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String imagePath;
    private String source;
    private String title;
    private String presentation;
    private AddressDto address;
    private String fileName;
    private String originalFileName;
    private String extension;
    private List<String> tags;
    private MultipartFile file;
    private Boolean isLinkedToUser;
    private ResumeDetailsDto details;
    private List<ResumeShareInfoDto> resumeShareInfos;
    private List<ResumeLinkedFileDto> additionalFiles;
    private String accountCode;
}
