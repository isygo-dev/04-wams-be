package eu.isygoit.mapper;

import eu.isygoit.dto.data.EmployeeDto;
import eu.isygoit.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Employee mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<Employee, EmployeeDto> {

}
