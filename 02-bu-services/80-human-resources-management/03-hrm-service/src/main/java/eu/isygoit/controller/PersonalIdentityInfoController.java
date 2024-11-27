package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedImageController;
import eu.isygoit.dto.data.PersonalIdentityInfoDto;
import eu.isygoit.exception.handler.HrmExceptionHandler;
import eu.isygoit.mapper.CinMapper;
import eu.isygoit.model.PersonalIdentityInfo;
import eu.isygoit.service.impl.PersonalIdentityInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Personal identity info controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = HrmExceptionHandler.class, mapper = CinMapper.class, minMapper = CinMapper.class, service = PersonalIdentityInfoService.class)
@RequestMapping(value = "/api/v1/private/cin")
public class PersonalIdentityInfoController extends MappedImageController<Long, PersonalIdentityInfo, PersonalIdentityInfoDto, PersonalIdentityInfoDto, PersonalIdentityInfoService> {


}
