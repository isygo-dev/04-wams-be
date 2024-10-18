package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.data.PaymentScheduleDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.PaymentBonusScheduleMapper;
import eu.isygoit.mapper.PaymentScheduleMapper;
import eu.isygoit.model.PaymentSchedule;
import eu.isygoit.service.IPaymentScheduleService;
import eu.isygoit.service.impl.PaymentScheduleService;
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
 * The type Payment schedule controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = PaymentScheduleMapper.class, minMapper = PaymentScheduleMapper.class, service = PaymentScheduleService.class)
@RequestMapping(value = "/api/v1/private/payment-Schedule")
public class PaymentScheduleController extends MappedCrudController<Long, PaymentSchedule, PaymentScheduleDto, PaymentScheduleDto, PaymentScheduleService> {
    @Autowired
    private IPaymentScheduleService paymentScheduleService;
    @Autowired
    private PaymentBonusScheduleMapper paymentBonusScheduleMapper;

    /**
     * Calculate payment schedule response entity.
     *
     * @param contractId the contract id
     * @return the response entity
     */
    @Operation(summary = "Calculate payment schedule Api",
            description = "Calculate payment schedule")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Api executed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @GetMapping(path = "/calculate/{contractId}")
    public ResponseEntity<List<PaymentScheduleDto>> calculatePaymentSchedule(@PathVariable(name = RestApiConstants.CONTRACT_ID) Long contractId) {
        try {
            return ResponseEntity.ok(mapper().listEntityToDto(paymentScheduleService.calculatePaymentSchedule(contractId)));
        } catch (Exception e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }

    }


}