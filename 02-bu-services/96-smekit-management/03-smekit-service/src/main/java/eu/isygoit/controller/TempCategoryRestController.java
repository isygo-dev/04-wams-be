package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.TempCategoryDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.AuthorMapper;
import eu.isygoit.mapper.TempCategoryMapper;
import eu.isygoit.service.impl.AuthorService;
import eu.isygoit.service.impl.CategoryService;
import eu.isygoit.model.TempCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = TempCategoryMapper.class, minMapper = TempCategoryMapper.class, service = AuthorService.class)
@RequestMapping(value = "/api/v1/private/category")

public class TempCategoryRestController extends MappedCrudController<Long, TempCategory, TempCategoryDto, TempCategoryDto, CategoryService> {
}
