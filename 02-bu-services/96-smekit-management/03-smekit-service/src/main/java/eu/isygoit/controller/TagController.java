package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.TagDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.TagMapped;
import eu.isygoit.model.Tag;
import eu.isygoit.service.impl.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = TagMapped.class, minMapper = TagMapped.class, service = TagService.class)
@RequestMapping(value = "/api/v1/private/tag")

public class TagController extends MappedCrudController <Long , Tag, TagDto , TagDto , TagService> {
}
