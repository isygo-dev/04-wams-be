package eu.isygoit.dto.data;

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
public class AuthorDto extends AbstractAuditableDto<Long> {
    private String name;
    private String description;
    private String url;
    private String code;
    private List<TemplateDto> templates;
}
