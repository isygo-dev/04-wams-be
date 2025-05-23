package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.DocumentMapper;
import eu.isygoit.model.Document;
import eu.isygoit.service.impl.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = DocumentMapper.class, minMapper = DocumentMapper.class, service = DocumentService.class)
@RequestMapping(value = "/api/v1/private/document")
public class DocumentController extends MappedCrudController<Long, Document, DocumentDto, DocumentDto, DocumentService> {
}
