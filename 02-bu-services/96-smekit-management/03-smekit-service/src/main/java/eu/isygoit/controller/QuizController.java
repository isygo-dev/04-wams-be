package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.api.QuizControllerApi;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.QuizMapper;
import eu.isygoit.model.Quiz;
import eu.isygoit.service.impl.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Quiz controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = QuizMapper.class, minMapper = QuizMapper.class, service = QuizService.class)
@RequestMapping(value = "/api/v1/private/quiz")
public class QuizController extends MappedCrudController<Long, Quiz, QuizDto, QuizDto, QuizService> implements QuizControllerApi {

}
