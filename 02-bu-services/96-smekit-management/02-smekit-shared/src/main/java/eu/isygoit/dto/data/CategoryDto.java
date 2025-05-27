package eu.isygoit.dto.data;

import eu.isygoit.dto.IImageUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumCategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class CategoryDto extends AbstractAuditableDto<Long> implements IImageUploadDto {

    List<String> tags;
    private String domain;
    private String name;
    private String description;
    @Builder.Default
    private IEnumCategoryStatus.Status status = IEnumCategoryStatus.Status.DISABLED;
    private String imagePath;
}
