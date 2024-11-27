package eu.isygoit.mapper;

import eu.isygoit.dto.data.ContractDto;
import eu.isygoit.model.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Contract mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface ContractMapper extends EntityMapper<Contract, ContractDto> {

}
