package eu.isygoit.dto.data;

import eu.isygoit.enums.IEnumTimeType;
import eu.isygoit.enums.IEnumWorkMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * The type Contract dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ContractDto extends MinContractDto {

    private String location;
    private IEnumWorkMode.Types workingMode;
    private IEnumTimeType.Types availability;
    private Integer probationaryPeriod;
    private Integer vacationBalance;
    private Boolean isRenewable;
    private SalaryInformationDto salaryInformation;
    private HolidayInformationDto holidayInformation;
    private List<PaymentScheduleDto> paymentSchedules;
    private List<AdvantageDto> advantages;
    private List<EquipmentDto> equipments;
    private List<ContractAmendmentDto> contractAmendments;
}
