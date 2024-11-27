package eu.isygoit.mapper;

import eu.isygoit.dto.data.PersonalIdentityInfoDto;
import eu.isygoit.model.PersonalIdentityInfo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Cin mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface CinMapper extends EntityMapper<PersonalIdentityInfo, PersonalIdentityInfoDto> {

}
