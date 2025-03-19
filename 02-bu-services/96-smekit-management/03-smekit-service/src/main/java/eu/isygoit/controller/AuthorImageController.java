package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedImageController;
import eu.isygoit.dto.data.AuthorDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.AuthorMapper;
import eu.isygoit.model.Author;
import eu.isygoit.service.impl.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = AuthorMapper.class, minMapper = AuthorMapper.class, service = AuthorService.class)
@RequestMapping(value = "/api/v1/private/Author")

public class    AuthorImageController extends MappedImageController<Long, Author, AuthorDto,AuthorDto, AuthorService> {
}
