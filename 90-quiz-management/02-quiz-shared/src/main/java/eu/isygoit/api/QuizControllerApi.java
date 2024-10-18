package eu.isygoit.api;

import eu.isygoit.com.rest.api.IMappedCrudApi;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.dto.data.QuizListDto;
import eu.isygoit.dto.data.QuizQuestionDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * The interface Quiz controller api.
 */
public interface QuizControllerApi extends IMappedCrudApi<Long, QuizDto, QuizDto> {

    /**
     * Download question image response entity.
     *
     * @param requestContext the request context
     * @param id             the id
     * @return the response entity
     * @throws IOException the io exception
     */
    @Operation(summary = "Download question image by linked object identifier",
            description = "xxx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/question/image/download/{id}")
    ResponseEntity<Resource> downloadQuestionImage(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                   @PathVariable(name = RestApiConstants.ID) Long id) throws IOException;

    /**
     * Upload question image response entity.
     *
     * @param requestContext the request context
     * @param id             the id
     * @param file           the file
     * @return the response entity
     */
    @Operation(summary = "Upload a new question image file and link it to an object with object identifier",
            description = "xxx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuizQuestionDto.class))})
    })
    @PostMapping(path = "/question/image/upload/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<QuizQuestionDto> uploadQuestionImage(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                        @PathVariable(name = RestApiConstants.ID) Long id,
                                                        @RequestPart(name = RestApiConstants.FILE) MultipartFile file);


    /**
     * Gets quiz codes by category.
     *
     * @param requestContext the request context
     * @param category       the category
     * @return the quiz codes by category
     */
    @Operation(summary = "get Quiz Codes By Category",
            description = "get Quiz Codes By Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/category")
    ResponseEntity<List<QuizListDto>> getQuizCodesByCategory(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                             @RequestParam(name = RestApiConstants.CATEGORY) String category);


    /**
     * Find by code response entity.
     *
     * @param requestContext the request context
     * @param code           the code
     * @return the response entity
     */
    @Operation(summary = "Find all objects with full data by object identifier (uses Full Dto)",
            description = "xxx")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/code/{code}")
    ResponseEntity<QuizDto> findByCodeIgnoreCase(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT, required = false) RequestContextDto requestContext,
                                                 @PathVariable(name = RestApiConstants.CODE) String code);
}
