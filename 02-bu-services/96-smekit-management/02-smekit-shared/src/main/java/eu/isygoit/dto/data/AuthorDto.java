package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthorDto extends AbstractAuditableDto<Long> implements IImageUploadDto, IFileUploadDto {
    private String firstname;
    private String lastname;
    private  String domain;
    private String code;
    private String imagePath ;
    private String email;
    private String phone;
    private String type;
    private MultipartFile file;
    private String originalFileName;
    private String path ;
    private String fileName ;
    private String extension;

}
