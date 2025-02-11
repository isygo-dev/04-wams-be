package eu.isygoit.dto.data;

import eu.isygoit.dto.extendable.AbstractAuditableDto;

import java.util.Set;

public class TagDto extends AbstractAuditableDto {
    private String tagName;
    private Set<TemplateDto> templates;

}
