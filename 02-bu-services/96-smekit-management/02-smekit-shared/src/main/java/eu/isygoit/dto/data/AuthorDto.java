package eu.isygoit.dto.data;

import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthorDto extends AbstractAuditableDto<Long> implements IImageUploadDto {
    private String firstname;
    private String lastname;
    private String description;
    private  String domain;
    private String code;
    private String imagePath ;
}
