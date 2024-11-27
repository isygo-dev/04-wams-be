package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.data.PaymentBonusScheduleDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.PaymentBonusScheduleMapper;
import eu.isygoit.model.PaymentBonusSchedule;
import eu.isygoit.service.impl.PaymentScheduleBonusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Payment bonus schedule controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = PaymentBonusScheduleMapper.class, minMapper = PaymentBonusScheduleMapper.class, service = PaymentScheduleBonusService.class)
@RequestMapping(value = "/api/v1/private/payment-Schedule/bonus")
public class PaymentBonusScheduleController extends MappedCrudController<Long, PaymentBonusSchedule, PaymentBonusScheduleDto, PaymentBonusScheduleDto, PaymentScheduleBonusService> {

    @Autowired
    private PaymentBonusScheduleMapper paymentBonusScheduleMapper;

    /**
     * Calculate bonus payment schedule response entity.
     *
     * @param contractId the contract id
     * @return the response entity
     */
    @Operation(summary = "Calculate Bonus Payment Schedule Api",
            description = "Calculate Bonus Payment Schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api executed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @GetMapping(path = "/calculate/{contractId}")
    public ResponseEntity<List<PaymentBonusScheduleDto>> calculateBonusPaymentSchedule(@PathVariable(name = RestApiConstants.CONTRACT_ID) Long contractId) {
        try {
            return ResponseEntity.ok(paymentBonusScheduleMapper.listEntityToDto(crudService().calculateBonusPaymentSchedule(contractId)));
        } catch (Exception e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }

    }

}