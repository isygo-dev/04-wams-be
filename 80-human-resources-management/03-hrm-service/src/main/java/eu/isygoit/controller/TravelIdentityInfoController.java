package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedImageController;
import eu.isygoit.dto.data.TravelIdentityInfoDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.PassportMapper;
import eu.isygoit.model.TravelIdentityInfo;
import eu.isygoit.service.impl.TravelIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Travel identity info controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = PassportMapper.class, minMapper = PassportMapper.class, service = TravelIdentityInfoService.class)
@RequestMapping(value = "/api/v1/private/passport")
public class TravelIdentityInfoController extends MappedImageController<Long, TravelIdentityInfo, TravelIdentityInfoDto, TravelIdentityInfoDto, TravelIdentityInfoService> {

}
