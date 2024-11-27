package eu.isygoit.mapper;

import eu.isygoit.dto.data.PaymentBonusScheduleDto;
import eu.isygoit.model.PaymentBonusSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Payment bonus schedule mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface PaymentBonusScheduleMapper extends EntityMapper<PaymentBonusSchedule, PaymentBonusScheduleDto> {

}
