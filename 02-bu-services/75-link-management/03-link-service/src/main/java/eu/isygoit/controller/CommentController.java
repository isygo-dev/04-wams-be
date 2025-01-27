package eu.isygoit.controller;


import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.api.CommentControllerApi;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.PostCommentDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.PostCommentNotFoundException;
import eu.isygoit.exception.handler.LinkExceptionHandler;
import eu.isygoit.mapper.PostCommentMapper;
import eu.isygoit.model.PostComment;
import eu.isygoit.service.IPostCommentService;
import eu.isygoit.service.IPostService;
import eu.isygoit.service.impl.PostCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The type Comment controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = LinkExceptionHandler.class, mapper = PostCommentMapper.class, minMapper = PostCommentMapper.class, service = PostCommentService.class)
@RequestMapping(value = "/api/v1/private/comment")
public class CommentController extends MappedCrudController<Long, PostComment, PostCommentDto, PostCommentDto, PostCommentService> implements CommentControllerApi {

    /**
     * The Comment mapper.
     */
    @Autowired
    PostCommentMapper commentMapper;
    /**
     * The Comment service.
     */
    @Autowired
    IPostCommentService commentService;
    /**
     * The Post service.
     */
    @Autowired
    IPostService postService;

    /**
     * Create like comment response entity.
     *
     * @param requestContext the request context
     * @param commentId      the comment id
     * @param accountCode    the account code
     * @return the response entity
     */
    @Operation(summary = "createLikeComment Api",
            description = "createLikeComment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/like")
    public ResponseEntity<PostCommentDto> createLikeComment(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                            @RequestParam(name = RestApiConstants.ID) Long commentId,
                                                            @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode
    ) {
        try {
            final Optional<PostComment> optional = commentService.findById(commentId);
            optional.ifPresentOrElse(postComment -> {
                        postComment.getUsersAccountCode().add(accountCode);
                    },
                    () -> new PostCommentNotFoundException("with id " + commentId)
            );

            return ResponseFactory.ResponseOk(commentMapper.entityToDto(commentService.update(optional.get())));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Gets liked comment by comment id.
     *
     * @param requestContext the request context
     * @param commentId      the comment id
     * @return the liked comment by comment id
     */
    @Operation(summary = "getListUsersLikedComment Api",
            description = "getListUsersLikedComment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/like/{commentId}")
    public ResponseEntity<List<String>> getLikedCommentByCommentId(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                                   @PathVariable(name = RestApiConstants.COMMENT_ID) Long commentId) {
        try {
            return ResponseFactory.ResponseOk(commentService.findById(commentId)
                    .orElseThrow(() -> new PostCommentNotFoundException("with id " + commentId))
                    .getUsersAccountCode());
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Create dislike comment response entity.
     *
     * @param requestContext the request context
     * @param commentId      the comment id
     * @param accountCode    the account code
     * @return the response entity
     */
    @Operation(summary = "createDislikeComment Api",
            description = "createDislikeComment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/dislike")
    public ResponseEntity<PostCommentDto> createDislikeComment(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                               @RequestParam(name = RestApiConstants.ID) Long commentId,
                                                               @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode
    ) {
        try {
            final Optional<PostComment> optional = commentService.findById(commentId);
            optional.ifPresentOrElse(postComment -> {
                        postComment.getUsersAccountCode().remove(accountCode);
                    },
                    () -> new PostCommentNotFoundException("with id " + commentId)
            );

            return ResponseFactory.ResponseOk(commentMapper.entityToDto(commentService.update(optional.get())));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

}
