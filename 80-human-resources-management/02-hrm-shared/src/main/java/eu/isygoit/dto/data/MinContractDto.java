package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumContractType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Min contract dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MinContractDto extends AbstractAuditableDto<Long> implements IFileUploadDto {

    private String code;
    private String domain;
    private IEnumContractType.Types contract;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long employee;
    private Boolean isLocked;
    private String fileName;
    private String originalFileName;
    private String extension;
    private MultipartFile file;
    private List<String> tags;
}
