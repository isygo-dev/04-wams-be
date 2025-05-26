package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.api.ContractControllerApi;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.ContractDto;
import eu.isygoit.dto.data.MinContractDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.ContractMapper;
import eu.isygoit.model.Contract;
import eu.isygoit.service.IContractService;
import eu.isygoit.service.impl.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Contract controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = ContractMapper.class, minMapper = ContractMapper.class, service = ContractService.class)
@RequestMapping(value = "/api/v1/private/contract")
public class ContractController extends MappedCrudController<Long, Contract, MinContractDto, ContractDto, ContractService>
        implements ContractControllerApi {
    @Autowired
    private IContractService contractService;

    @Override
    public ResponseEntity<ContractDto> updateContractStatus(RequestContextDto requestContext,
                                                            Long id, Boolean isLocked) {
        log.info("update contrat status");
        try {
            return ResponseFactory.responseOk(mapper().entityToDto(contractService.updateContractStatus(id, isLocked)));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
