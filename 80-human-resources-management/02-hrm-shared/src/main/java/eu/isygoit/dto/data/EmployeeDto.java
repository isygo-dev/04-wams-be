package eu.isygoit.dto.data;

import eu.isygoit.dto.IFileUploadDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The type Employee dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EmployeeDto extends MinEmployeeDto implements IFileUploadDto {

    private String phone;
    private AddressDto address;
    private EmployeeDetailsDto details;
    private List<ContractDto> contracts;
    private Boolean isLinkedToUser;
    private String fileName;
    private String originalFileName;
    private MultipartFile file;
    private List<EmployeeLinkedFileDto> additionalFiles;
}
