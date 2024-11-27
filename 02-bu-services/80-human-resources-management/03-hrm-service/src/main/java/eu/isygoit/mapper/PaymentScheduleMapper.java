package eu.isygoit.mapper;

import eu.isygoit.dto.data.PaymentScheduleDto;
import eu.isygoit.model.PaymentSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Payment schedule mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface PaymentScheduleMapper extends EntityMapper<PaymentSchedule, PaymentScheduleDto> {

}
