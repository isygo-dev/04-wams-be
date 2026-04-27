package eu.isygoit.api;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.CandidateQuizAnswerDto;
import eu.isygoit.dto.data.CandidateQuizDto;
import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.dto.data.QuizReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The interface Candidate quiz controller api.
 */
public interface CandidateQuizControllerApi extends IMappedCrudApi<Long, CandidateQuizDto, CandidateQuizDto> {

    /**
     * Gets candidate quiz.
     
     * @param quizCode       the quiz code
     * @param accountCode    the account code
     * @return the candidate quiz
     */
    @Operation(summary = "Get candidate quiz copy",
            description = "This endpoint retrieves a copy of the quiz for the specified candidate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz copy retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizDto.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/copy")
    ResponseEntity<QuizDto> getCandidateQuiz(
                                             @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                             @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode);

    /**
     * Submit candidate quiz response entity.
     
     * @param quizCode       the quiz code
     * @param accountCode    the account code
     * @return the response entity
     */
    @Operation(summary = "Submit candidate quiz",
            description = "This endpoint submits the entire quiz for the specified candidate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz submitted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PutMapping(path = "/submit")
    ResponseEntity<Boolean> submitCandidateQuiz(
                                                @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                                @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode);

    /**
     * Start candidate quiz response entity.
     
     * @param quizCode       the quiz code
     * @param accountCode    the account code
     * @return the response entity
     */
    @Operation(summary = "Start candidate quiz",
            description = "This endpoint marks the start of the quiz for the specified candidate and returns the session ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz started successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping(path = "/start")
    ResponseEntity<Long> startCandidateQuiz(
                                            @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                            @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode);


    /**
     * Submit candidate quiz answer response entity.
     *
     * @param quizCode    the quiz code
     * @param accountCode the account code
     * @param answer      the answer
     * @return the response entity
     */
    @Operation(summary = "Submit candidate quiz answer",
            description = "This endpoint submits a single answer for a quiz by a specific candidate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Answer submitted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PutMapping(path = "/answer/submit")
    ResponseEntity<Boolean> submitCandidateQuizAnswer(//
                                                      @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                                      @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode,
                                                      @Valid @RequestBody CandidateQuizAnswerDto answer);

    /**
     * Submit candidate quiz answer list response entity.
     *
     * @param quizCode    the quiz code
     * @param accountCode the account code
     * @param answers     the answers
     * @return the response entity
     */
    @Operation(summary = "Submit list of candidate quiz answers",
            description = "This endpoint submits a list of answers for a quiz by a specific candidate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Answers submitted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PutMapping(path = "/answer/list/submit")
    ResponseEntity<Boolean> submitCandidateQuizAnswerList(//
                                                          @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                                          @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode,
                                                          @Valid @RequestBody List<CandidateQuizAnswerDto> answers);

    /**
     * Start candidate quiz answer response entity.
     *
     * @param quizCode    the quiz code
     * @param accountCode the account code
     * @param answer      the answer
     * @return the response entity
     */
    @Operation(summary = "Start candidate quiz answer session",
            description = "This endpoint starts a session for a specific answer in a quiz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Answer session started successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping(path = "/answer/start")
    ResponseEntity<Boolean> startCandidateQuizAnswer(//
                                                     @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                                     @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode,
                                                     @Valid @RequestBody CandidateQuizAnswerDto answer);

    /**
     * Gets complete answer.
     
     * @param quizCode       the quiz code
     * @param accountCode    the account code
     * @return the complete answer
     */
    @Operation(summary = "Get complete answers for candidate quiz",
            description = "This endpoint retrieves the complete set of answers provided by a candidate for a specific quiz.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Answers retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No answers found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/complete")
    ResponseEntity<QuizDto> getCompleteAnswer(
                                              @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                              @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode);

    /**
     * Gets complete answer.
     
     * @param quizCode       the quiz code
     * @param accountCode    the account code
     * @return the complete answer
     */
    @Operation(summary = "Get clean complete answers for candidate quiz",
            description = "This endpoint retrieves the complete set of answers provided by a candidate for a specific quiz, with sensitive information removed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Clean answers retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No answers found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/complete/clean")
    ResponseEntity<QuizDto> getCompleteAnswerClean(
                                                   @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                                   @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode);

    /**
     * Gets report answer.
     
     * @param quizCode       the quiz code
     * @param accountCode    the account code
     * @return the report answer
     */
    @Operation(summary = "Get candidate quiz report",
            description = "This endpoint retrieves the report/results of a quiz taken by a specific candidate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Quiz report retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizReportDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No report found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/report")
    ResponseEntity<QuizReportDto> getReportAnswer(
                                                  @RequestParam(name = RestApiConstants.QUIZ_CODE) String quizCode,
                                                  @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode);

    /**
     * Gets by candidate and tags.
     
     * @param accountCode    the account code
     * @param tags           the tags
     * @return the by candidate and tags
     */
    @Operation(summary = "Get reports by candidate and tags",
            description = "This endpoint retrieves a list of quiz reports for a specific candidate, filtered by tags.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Reports retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizReportDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No reports found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/tags")
    ResponseEntity<List<QuizReportDto>> getByCandidateAndTags(
                                                              @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode,
                                                              @RequestParam(name = RestApiConstants.TAGS) List<String> tags);

    @GetMapping(path = "/total")
    ResponseEntity<Long> getCountRealizedTestByAccount(@RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode);
}
