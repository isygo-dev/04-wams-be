package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedImageController;
import eu.isygoit.dto.data.PostDto;
import eu.isygoit.exception.handler.LinkExceptionHandler;
import eu.isygoit.mapper.PostMapper;
import eu.isygoit.model.Post;
import eu.isygoit.service.impl.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Post image controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = LinkExceptionHandler.class, mapper = PostMapper.class, minMapper = PostMapper.class, service = PostService.class)
@RequestMapping(value = "/api/v1/private/post")
public class PostImageController extends MappedImageController<Long, Post, PostDto, PostDto, PostService> {

}
