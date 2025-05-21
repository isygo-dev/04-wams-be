package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.RequestContextDto;
import eu.isygoit.dto.data.ResumeDto;
import eu.isygoit.dto.data.ResumeShareInfoDto;
import eu.isygoit.dto.extendable.IdentifiableDto;
import eu.isygoit.dto.request.ShareResumeRequestDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.ResumeMapper;
import eu.isygoit.mapper.ResumeShareInfoMapper;
import eu.isygoit.model.Resume;
import eu.isygoit.model.ResumeShareInfo;
import eu.isygoit.repository.ResumeSharedInfoRepository;
import eu.isygoit.service.impl.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * The type Resume controller.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/api/v1/private/resume")
@CtrlDef(handler = RpmExceptionHandler.class, mapper = ResumeMapper.class, minMapper = ResumeMapper.class, service = ResumeService.class)
public class ResumeController extends MappedCrudController<Long, Resume, ResumeDto, ResumeDto, ResumeService> {

    @Autowired
    private ResumeSharedInfoRepository resumeSharedInfoRepository;
    @Autowired
    private ResumeShareInfoMapper resumeShareInfoMapper;

    /**
     * Share resume response entity.
     *
     * @param requestContext        the request context
     * @param id                    the id
     * @param shareResumeRequestDto the share resume request dto
     * @return the response entity
     */
    @Operation(summary = "shareResume Api",
            description = "shareResume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/share/{id}")
    public ResponseEntity<List<ResumeShareInfoDto>> shareResume(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                                @PathVariable(name = RestApiConstants.ID) Long id,
                                                                @Valid @RequestBody ShareResumeRequestDto shareResumeRequestDto) {
        log.info("share resume ");
        try {
            return ResponseFactory.ResponseOk(resumeShareInfoMapper.listEntityToDto(crudService().shareWithAccounts(
                    id, shareResumeRequestDto.getResumeOwner(), shareResumeRequestDto.getAccountsCode())));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public List<ResumeDto> afterFindAll(RequestContextDto requestContext, List<ResumeDto> resumeDtos) {
        return super.afterFindAll(requestContext, resumeDtos.stream()
                .map(resumeDto -> {
                    String accountCode = crudService().getAccountCode(resumeDto.getCode());
                    resumeDto.setAccountCode(accountCode);
                    return super.afterFindById(resumeDto);
                })
                .collect(Collectors.toUnmodifiableList()));
    }

    @Override
    public ResumeDto afterFindById(ResumeDto resumeDto) {
        resumeDto.setAccountCode(crudService().getAccountCode(resumeDto.getCode()));
        return super.afterFindById(resumeDto);
    }

    /**
     * Update resume review response entity.
     *
     * @param requestContext  the request context
     * @param id              the id
     * @param resumeShareInfo the resume share info
     * @return the response entity
     */
    @Operation(summary = "updateResumeReview Api",
            description = "updateResumeReview")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PutMapping(path = "/resume-review/update/{id}")
    public ResponseEntity<ResumeShareInfo> updateResumeReview(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                              @PathVariable(name = RestApiConstants.ID) Long id,
                                                              @Valid @RequestBody ResumeShareInfo resumeShareInfo) {
        log.info("update resume review");
        try {
            ResumeShareInfo resumeDto = resumeSharedInfoRepository.save(resumeShareInfo);
            return ResponseFactory.ResponseOk(resumeDto);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Gets resume by account code.
     *
     * @param requestContext the request context
     * @return the resume by account code
     */
    @Operation(summary = "findCurrentUserResume Api",
            description = "findCurrentUserResume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @GetMapping(path = "/findByCandidateCode")
    public ResponseEntity<ResumeDto> getResumeByAccountCode(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext) {
        try {
            ResumeDto resumeDto = mapper().entityToDto(crudService().getResumeByAccountCode(requestContext.getSenderUser()));
            if (resumeDto != null) {
                return ResponseFactory.ResponseOk(resumeDto);
            }
            return ResponseEntity.noContent().build();
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Create current user resume response entity.
     *
     * @param requestContext the request context
     * @param resumeDto      the resume dto
     * @return the response entity
     */
    @Operation(summary = "createCurrentUserResume Api",
            description = "createCurrentUserResume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/candidate")
    public ResponseEntity<ResumeDto> createCurrentUserResume(@RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
                                                             @Valid @RequestBody ResumeDto resumeDto) {
        try {
            if (!StringUtils.hasText(resumeDto.getDomain())) {
                resumeDto.setDomain(requestContext.getSenderDomain());
            }
            return ResponseFactory.ResponseOk(mapper().entityToDto(crudService().createResumeForAccount(requestContext.getSenderUser(),
                    mapper().dtoToEntity(resumeDto))));
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }

    /**
     * Assign account to resume response entity.
     *
     * @param requestContext the request context
     * @param resumeDto      the resume dto
     * @return the response entity
     */
    @Operation(summary = "create Account to resume Api",
            description = "create account toResume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Api executed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IdentifiableDto.class))})
    })
    @PostMapping(path = "/create/account")
    public ResponseEntity<Boolean> assignAccountToResume(
            @RequestAttribute(value = JwtConstants.JWT_USER_CONTEXT) RequestContextDto requestContext,
            @Valid @RequestBody ResumeDto resumeDto) {
        try {
            crudService().createAccount(mapper().dtoToEntity(resumeDto));
            return ResponseFactory.ResponseOk(Boolean.TRUE);
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }
}
