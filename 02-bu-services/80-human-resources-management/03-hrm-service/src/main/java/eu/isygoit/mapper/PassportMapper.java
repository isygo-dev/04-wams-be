package eu.isygoit.mapper;

import eu.isygoit.dto.data.TravelIdentityInfoDto;
import eu.isygoit.model.TravelIdentityInfo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Passport mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface PassportMapper extends EntityMapper<TravelIdentityInfo, TravelIdentityInfoDto> {

}
