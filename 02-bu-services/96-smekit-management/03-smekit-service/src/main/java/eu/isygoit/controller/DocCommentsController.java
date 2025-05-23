package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.DocCommentDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocCommentsMapper;
import eu.isygoit.model.DocComment;
import eu.isygoit.service.impl.DocCommentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocCommentsMapper.class, minMapper = DocCommentsMapper.class, service = DocCommentsService.class)
@RequestMapping(value = "/api/v1/private/docComments")
public class DocCommentsController extends MappedCrudController<Long, DocComment, DocCommentDto, DocCommentDto, DocCommentsService> {
}
