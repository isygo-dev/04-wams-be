package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.api.CandidateQuizControllerApi;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.CandidateQuizAnswerDto;
import eu.isygoit.dto.data.CandidateQuizDto;
import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.dto.data.QuizReportDto;
import eu.isygoit.exception.handler.QuizExceptionHandler;
import eu.isygoit.mapper.CandidateQuizAnswerMapper;
import eu.isygoit.mapper.CandidateQuizMapper;
import eu.isygoit.model.CandidateQuiz;
import eu.isygoit.service.impl.CandidateQuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Candidate quiz controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = QuizExceptionHandler.class, mapper = CandidateQuizMapper.class, minMapper = CandidateQuizMapper.class, service = CandidateQuizService.class)
@RequestMapping(value = "/api/v1/private/candidate/quiz")
public class CandidateQuizController extends MappedCrudController<Long, CandidateQuiz, CandidateQuizDto, CandidateQuizDto, CandidateQuizService>
        implements CandidateQuizControllerApi {

    @Autowired
    private CandidateQuizAnswerMapper candidateQuizAnswerMapper;

    @Override
    public ResponseEntity<QuizDto> getCandidateQuiz(RequestContextDto requestContext,
                                                    String quizCode, String accountCode) {
        try {
            return ResponseFactory.responseOk(crudService().getCandidateQuiz(quizCode, accountCode));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> submitCandidateQuiz(RequestContextDto requestContext,
                                                       String quizCode, String accountCode) {
        try {
            return ResponseFactory.responseOk(crudService().submitCandidateQuiz(quizCode, accountCode));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<Long> startCandidateQuiz(RequestContextDto requestContext,
                                                   String quizCode, String accountCode) {
        try {
            return ResponseFactory.responseOk(crudService().startCandidateQuiz(quizCode, accountCode));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> submitCandidateQuizAnswer(//RequestContextDto requestContext,
                                                             String quizCode, String accountCode, CandidateQuizAnswerDto answer) {
        try {
            return ResponseFactory.responseOk(crudService().submitCandidateAnswer(quizCode, accountCode,
                    candidateQuizAnswerMapper.dtoToEntity(answer)));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> submitCandidateQuizAnswerList(String quizCode, String accountCode, List<CandidateQuizAnswerDto> answers) {
        try {
            return ResponseFactory.responseOk(crudService().submitCandidateAnswers(quizCode, accountCode,
                    candidateQuizAnswerMapper.listDtoToEntity(answers)));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<Boolean> startCandidateQuizAnswer(//RequestContextDto requestContext,
                                                            String quizCode, String accountCode, CandidateQuizAnswerDto answer) {
        try {
            return ResponseFactory.responseOk(crudService().startCandidateAnswer(quizCode, accountCode,
                    candidateQuizAnswerMapper.dtoToEntity(answer)));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<QuizDto> getCompleteAnswer(RequestContextDto requestContext,
                                                     String quizCode, String accountCode) {
        try {
            QuizDto completeAnswer = crudService().getCompleteAnswer(quizCode, accountCode);
            if (completeAnswer != null) {
                return ResponseFactory.responseOk(completeAnswer);
            } else {
                return ResponseFactory.responseNoContent();
            }
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<QuizDto> getCompleteAnswerClean(RequestContextDto requestContext,
                                                          String quizCode, String accountCode) {
        try {
            QuizDto completeAnswer = crudService().getCompleteAnswerClean(quizCode, accountCode);
            if (completeAnswer != null) {
                return ResponseFactory.responseOk(completeAnswer);
            } else {
                return ResponseFactory.responseNoContent();
            }
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<QuizReportDto> getReportAnswer(RequestContextDto requestContext,
                                                         String quizCode, String accountCode) {
        try {
            QuizReportDto quizReport = crudService().getReport(quizCode, accountCode);
            if (quizReport != null) {
                return ResponseFactory.responseOk(quizReport);
            } else {
                return ResponseFactory.responseNoContent();
            }
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<List<QuizReportDto>> getByCandidateAndTags(RequestContextDto requestContext,
                                                                     String accountCode, List<String> tags) {
        try {
            List<QuizReportDto> list = crudService().getByCandidateAndTags(accountCode, tags);
            if (!CollectionUtils.isEmpty(list)) {
                return ResponseFactory.responseOk(list);
            } else {
                return ResponseFactory.responseNoContent();
            }
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public ResponseEntity<Long> getCountRealizedTestByAccount(String accountCode) {
        try {
            return ResponseFactory.responseOk(crudService().getCountRealizedTestByAccount(accountCode));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
