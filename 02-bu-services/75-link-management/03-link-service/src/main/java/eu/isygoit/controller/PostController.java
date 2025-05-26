package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.api.PostControllerApi;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.PostDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.exception.PostNotFoundException;
import eu.isygoit.exception.handler.LinkExceptionHandler;
import eu.isygoit.mapper.PostMapper;
import eu.isygoit.model.Post;
import eu.isygoit.service.IPostService;
import eu.isygoit.service.impl.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The type Post controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = LinkExceptionHandler.class, mapper = PostMapper.class, minMapper = PostMapper.class, service = PostService.class)
@RequestMapping(value = "/api/v1/private/post")
public class PostController extends MappedCrudController<Long, Post, PostDto, PostDto, PostService> implements PostControllerApi {

    @Autowired
    private IPostService postService;
    @Autowired
    private PostMapper postMapper;

    /**
     * Create like post response entity.
     *
     * @param requestContext the request context
     * @param postId         the post id
     * @param accountCode    the account code
     * @param isLike         the is like
     * @return the response entity
     */
    @Operation(summary = "createLikePost Api",
            description = "createLikePost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/like")
    public ResponseEntity<PostDto> createLikePost(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                  @RequestParam(name = RestApiConstants.ID) Long postId,
                                                  @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode,
                                                  @RequestParam Boolean isLike
    ) {
        try {
            Optional<Post> optional = postService.findById(postId);
            optional.ifPresentOrElse(post -> {
                        final boolean b = isLike
                                ? (post.getUsersAccountCode().add(accountCode))
                                : (post.getUsersAccountCode().remove(accountCode));
                    },
                    () -> {
                        throw new PostNotFoundException("with id " + postId);
                    }
            );

            return ResponseFactory.responseOk(postMapper.entityToDto(postService.update(optional.get())));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Gets users liked post by post id.
     *
     * @param requestContext the request context
     * @param postId         the post id
     * @return the users liked post by post id
     */
    @Operation(summary = "getListUsersLikedPost Api",
            description = "getListUsersLikedPost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/like/{postId}")
    public ResponseEntity<List<String>> getUsersLikedPostByPostId(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                                  @PathVariable(name = RestApiConstants.POST_ID) Long postId) {
        try {
            return ResponseFactory.responseOk(postService.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("with id " + postId))
                    .getUsersAccountCode());
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Create dislike post response entity.
     *
     * @param requestContext the request context
     * @param postId         the post id
     * @param accountCode    the account code
     * @param isLike         the is like
     * @return the response entity
     */
    @Operation(summary = "createDislikePost Api",
            description = "createDislikePost")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/dislike")
    public ResponseEntity<PostDto> createDislikePost(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                     @RequestParam(name = RestApiConstants.ID) Long postId,
                                                     @RequestParam(name = RestApiConstants.ACCOUNT_CODE) String accountCode,
                                                     @RequestParam Boolean isLike) {
        try {
            final Optional<Post> optional = postService.findById(postId);
            final List<String> accountList = optional.get().getUsersAccountCodeDislike();
            optional.ifPresentOrElse(post -> {
                        final boolean b = isLike
                                ? (accountList.add(accountCode))
                                : (accountList.remove(accountCode));
                    },
                    () -> {
                        throw new PostNotFoundException("with id " + postId);
                    }
            );

            return ResponseFactory.responseOk(postMapper.entityToDto(postService.update(optional.get())));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    /**
     * Find all blogs response entity.
     *
     * @param requestContext the request context
     * @param page           the page
     * @param size           the size
     * @return the response entity
     */
    @Operation(summary = "Find all objects with minimal data by page (uses Min Dto)",
            description = "Find all objects with minimal data by page (uses Min Dto)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/blog/{page}/{size}")
    public ResponseEntity<List<PostDto>> findAllBlogs(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                      @PathVariable(name = RestApiConstants.PAGE) Integer page,
                                                      @PathVariable(name = RestApiConstants.SIZE) Integer size) {
        log.info("Find all post/blogs by page/size request received {}/{}", page, size);
        try {
            List<PostDto> list = null;
            if (DomainConstants.SUPER_DOMAIN_NAME.equals(requestContext.getSenderDomain())) {
                list = this.mapper().listEntityToDto(this.crudService().findByIsBlogTrue(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"))));
            } else {
                list = this.mapper().listEntityToDto(this.crudService().findByDomainAndIsBlogTrue(requestContext.getSenderDomain(), PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"))));
            }

            if (CollectionUtils.isEmpty(list)) {
                return ResponseFactory.responseNoContent();
            }

            this.afterFindAll(requestContext, list);
            return ResponseFactory.responseOk(list);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }
}
