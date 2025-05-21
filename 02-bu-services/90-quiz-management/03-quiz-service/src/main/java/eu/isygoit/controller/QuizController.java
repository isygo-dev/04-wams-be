package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.api.QuizControllerApi;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.dto.data.QuizListDto;
import eu.isygoit.dto.data.QuizQuestionDto;
import eu.isygoit.dto.data.QuizReportDto;
import eu.isygoit.exception.handler.QuizExceptionHandler;
import eu.isygoit.mapper.QuizListMapper;
import eu.isygoit.mapper.QuizMapper;
import eu.isygoit.mapper.QuizQuestionMapper;
import eu.isygoit.model.Quiz;
import eu.isygoit.service.ICandidateQuizService;
import eu.isygoit.service.impl.QuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * The type Quiz controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = QuizExceptionHandler.class, mapper = QuizMapper.class, minMapper = QuizMapper.class, service = QuizService.class)
@RequestMapping(value = "/api/v1/private/quiz")
public class QuizController extends MappedCrudController<Long, Quiz, QuizDto, QuizDto, QuizService>
        implements QuizControllerApi {

    @Autowired
    private QuizQuestionMapper quizQuestionMapper;
    @Autowired
    private ICandidateQuizService candidateQuizService;
    @Autowired
    private QuizListMapper quizListMapper;

    @Override
    public ResponseEntity<Resource> downloadQuestionImage(RequestContextDto requestContext, Long id) throws IOException {
        log.info("Download question image request received");
        try {
            Resource imageResource = crudService().downloadQuestionImage(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(imageResource.getFile().toPath()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageResource.getFilename() + "\"")
                    .body(imageResource);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<QuizQuestionDto> uploadQuestionImage(RequestContextDto requestContext, Long id, MultipartFile file) {
        log.info("Upload question image request received");
        try {
            return ResponseFactory.ResponseOk(quizQuestionMapper.entityToDto(crudService().uploadQuestionImage(id,
                    requestContext.getSenderDomain(), file)));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<List<QuizListDto>> getQuizCodesByCategory(RequestContextDto requestContext, String category) {
        log.info("get Quiz Codes By Category request received");
        try {
            List<Quiz> list = crudService().getQuizCodesByCategory(category);
            if (CollectionUtils.isEmpty(list)) {
                return ResponseFactory.ResponseNoContent();
            }
            return ResponseFactory.ResponseOk(quizListMapper.listEntityToDto(list));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<QuizDto> findByCodeIgnoreCase(RequestContextDto requestContext, String code) {
        log.info("get quiz by code received");
        try {
            QuizDto quizDto = mapper().entityToDto(crudService().getQuizByCode(code));
            if (Objects.isNull(quizDto)) {
                return ResponseFactory.ResponseNoContent();
            }

            return ResponseFactory.ResponseOk(this.afterFindById(quizDto));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public List<QuizDto> afterFindAll(RequestContextDto requestContext, List<QuizDto> list) {
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(quizDto -> {
                QuizReportDto quizReport = candidateQuizService.getReport(quizDto.getCode(), requestContext.getSenderUser());
                if (quizReport != null) {
                    quizDto.setStartDate(quizReport.getStartDate());
                    quizDto.setSubmitDate(quizReport.getSubmitDate());
                    quizDto.setScore(quizReport.getScore());
                    quizDto.setScale(quizReport.getScale());
                }
            });
        }
        return super.afterFindAll(requestContext, list);
    }
}
