package eu.isygoit.mapper;

import eu.isygoit.dto.data.EmployeeLinkedFileDto;
import eu.isygoit.model.EmployeeLinkedFile;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Employee linked file mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface EmployeeLinkedFileMapper extends EntityMapper<EmployeeLinkedFile, EmployeeLinkedFileDto> {
}
