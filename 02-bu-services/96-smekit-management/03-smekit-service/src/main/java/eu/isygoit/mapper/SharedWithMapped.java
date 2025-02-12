package eu.isygoit.mapper;

import eu.isygoit.dto.data.SharedWithDto;
import eu.isygoit.model.SharedWith;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public  interface SharedWithMapped extends EntityMapper<SharedWith, SharedWithDto>{

}
