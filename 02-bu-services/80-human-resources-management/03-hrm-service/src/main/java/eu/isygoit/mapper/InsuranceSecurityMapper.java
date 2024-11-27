package eu.isygoit.mapper;

import eu.isygoit.dto.data.InsuranceIdentityInfoDto;
import eu.isygoit.model.InsuranceIdentityInfo;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Insurance security mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface InsuranceSecurityMapper extends EntityMapper<InsuranceIdentityInfo, InsuranceIdentityInfoDto> {

}
