package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.BlogTalkDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
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
@CtrlDef(handler = LinkExceptionHandler.class, mapper = BlogTalkMapper.class, minMapper = BlogTalkMapper.class, service = BlogTalkService.class)
@RequestMapping(value = "/api/v1/private/blog/talk")
public class BlogTalkController extends MappedCrudController<UUID, BlogTalk, BlogTalkDto, BlogTalkDto, BlogTalkService> {

    /**
     * Find all by blog id response entity.
     *
     * @param requestContext the request context
     * @param blogId         the blog id
     * @param page           the page
     * @param size           the size
     * @return the response entity
     */
    @Operation(summary = "Find all BlogTalk by page",
            description = "Find all BlogTalk by page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/blogId/page")
    public ResponseEntity<List<BlogTalkDto>> findAllByBlogId(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                             @RequestParam(name = RestApiConstants.BLOG_ID) Long blogId,
                                                             @RequestParam(name = RestApiConstants.PAGE) Integer page,
                                                             @RequestParam(name = RestApiConstants.SIZE) Integer size) {
        try {
            List<BlogTalk> list = this.crudService().findByBlogId(blogId, page, size);
            if (!CollectionUtils.isEmpty(list)) {
                return ResponseFactory.ResponseOk(this.mapper().listEntityToDto(list));
            } else {
                return ResponseFactory.ResponseNoContent();
            }
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }

    /**
     * Find all by blog id response entity.
     *
     * @param requestContext the request context
     * @param blogId         the blog id
     * @return the response entity
     */
    @Operation(summary = "Find all BlogTalk by page",
            description = "Find all BlogTalk by page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/blogId")
    public ResponseEntity<List<BlogTalkDto>> findAllByBlogId(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                             @RequestParam(name = RestApiConstants.PAGE) Long blogId) {
        try {
            List<BlogTalk> list = this.crudService().findByBlogId(blogId);
            if (!CollectionUtils.isEmpty(list)) {
                return ResponseFactory.ResponseOk(this.mapper().listEntityToDto(list));
            } else {
                return ResponseFactory.ResponseNoContent();
            }
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }
}
