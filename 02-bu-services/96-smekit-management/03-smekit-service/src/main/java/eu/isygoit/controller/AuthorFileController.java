package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedFileController;
import eu.isygoit.dto.data.AuthorDto;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.AuthorMapper;
import eu.isygoit.mapper.TemplateMapper;
import eu.isygoit.model.Author;
import eu.isygoit.model.Template;
import eu.isygoit.service.impl.AuthorService;
import eu.isygoit.service.impl.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/author")
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = AuthorMapper.class, minMapper = AuthorMapper.class, service = AuthorService.class)
public class AuthorFileController extends MappedFileController<Long, Author, AuthorDto, AuthorDto, AuthorService> {
}
