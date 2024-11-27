package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.ContractDto;
import eu.isygoit.dto.data.MinContractDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.ContractMapper;
import eu.isygoit.model.Contract;
import eu.isygoit.service.impl.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Contract file controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = ContractMapper.class, minMapper = ContractMapper.class, service = ContractService.class)
@RequestMapping(value = "/api/v1/private/contract")
public class ContractFileController extends MappedFileController<Long, Contract, MinContractDto, ContractDto, ContractService> {

}
