package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedImageController;
import eu.isygoit.dto.data.InsuranceIdentityInfoDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.InsuranceSecurityMapper;
import eu.isygoit.model.InsuranceIdentityInfo;
import eu.isygoit.service.impl.InsuranceIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Insurance identity info controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = InsuranceSecurityMapper.class, minMapper = InsuranceSecurityMapper.class, service = InsuranceIdentityInfoService.class)
@RequestMapping(value = "/api/v1/private/security")
public class InsuranceIdentityInfoController extends MappedImageController<Long, InsuranceIdentityInfo, InsuranceIdentityInfoDto, InsuranceIdentityInfoDto, InsuranceIdentityInfoService> {


}
