package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.BlogDto;
import eu.isygoit.exception.handler.LinkExceptionHandler;
import eu.isygoit.mapper.BlogMapper;
import eu.isygoit.model.Blog;
import eu.isygoit.service.impl.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Blog controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = LinkExceptionHandler.class, mapper = BlogMapper.class, minMapper = BlogMapper.class, service = BlogService.class)
@RequestMapping(value = "/api/v1/private/blog")
public class BlogController extends MappedCrudController<Long, Blog, BlogDto, BlogDto, BlogService> {

}
