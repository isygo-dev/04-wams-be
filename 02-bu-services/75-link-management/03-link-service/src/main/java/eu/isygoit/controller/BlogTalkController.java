package eu.isygoit.controller;

import eu.isygoit.annotation.InjectMapperAndService;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.BlogTalkDto;
import eu.isygoit.exception.handler.LinkExceptionHandler;
import eu.isygoit.mapper.BlogTalkMapper;
import eu.isygoit.model.BlogTalk;
import eu.isygoit.service.impl.BlogTalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * The type Blog talk controller.
 */
@Slf4j
@Validated
@RestController
@InjectMapperAndService(handler = LinkExceptionHandler.class, mapper = BlogTalkMapper.class, minMapper = BlogTalkMapper.class, service = BlogTalkService.class)
@RequestMapping(value = "/api/v1/private/blog/talk")
public class BlogTalkController extends MappedCrudController<UUID, BlogTalk, BlogTalkDto, BlogTalkDto, BlogTalkService> {

    /**
     * Find all by blog id response entity.
     
     * @param blogId         the blog id
     * @param page           the page
     * @param size           the size
     * @return the response entity
     */
    @Operation(summary = "Find all blog talks by blog ID and page",
            description = "This endpoint retrieves a paginated list of blog talks associated with a specific blog ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Blog talks retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogTalkDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No blog talks found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/blogId/page")
    public ResponseEntity<List<BlogTalkDto>> findAllByBlogId(
                                                             @RequestParam(name = RestApiConstants.BLOG_ID) Long blogId,
                                                             @RequestParam(name = RestApiConstants.PAGE) Integer page,
                                                             @RequestParam(name = RestApiConstants.SIZE) Integer size) {
        try {
            List<BlogTalk> list = this.crudService().findByBlogId(blogId, page, size);
            if (!CollectionUtils.isEmpty(list)) {
                return ResponseFactory.responseOk(this.mapper().listEntityToDto(list));
            } else {
                return ResponseFactory.responseNoContent();
            }
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }

    /**
     * Find all by blog id response entity.
     
     * @param blogId         the blog id
     * @return the response entity
     */
    @Operation(summary = "Find all blog talks by blog ID",
            description = "This endpoint retrieves all blog talks associated with a specific blog ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Blog talks retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogTalkDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No blog talks found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/blogId")
    public ResponseEntity<List<BlogTalkDto>> findAllByBlogId(
                                                             @RequestParam(name = RestApiConstants.PAGE) Long blogId) {
        try {
            List<BlogTalk> list = this.crudService().findByBlogId(blogId);
            if (!CollectionUtils.isEmpty(list)) {
                return ResponseFactory.responseOk(this.mapper().listEntityToDto(list));
            } else {
                return ResponseFactory.responseNoContent();
            }
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }
}
