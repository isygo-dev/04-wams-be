package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.DocumentCommentDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocumentCommentMapper;
import eu.isygoit.model.DocumentComment;
import eu.isygoit.service.impl.DocumentCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocumentCommentMapper.class, minMapper = DocumentCommentMapper.class, service = DocumentCommentService.class)
@RequestMapping(value = "/api/v1/private/docComments")
public class DocCommentsController extends MappedCrudController<Long, DocumentComment, DocumentCommentDto, DocumentCommentDto, DocumentCommentService> {
}
