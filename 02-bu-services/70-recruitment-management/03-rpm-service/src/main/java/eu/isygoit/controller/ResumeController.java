package eu.isygoit.controller;

import eu.isygoit.annotation.InjectMapperAndService;
import eu.isygoit.com.rest.controller.ResponseFactory;
import eu.isygoit.com.rest.controller.constants.CtrlConstants;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.constants.JwtConstants;
import eu.isygoit.constants.RestApiConstants;
import eu.isygoit.dto.common.ContextRequestDto;
import eu.isygoit.dto.data.ResumeDto;
import eu.isygoit.dto.data.ResumeShareInfoDto;
import eu.isygoit.dto.extendable.IdAssignableDto;
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
@InjectMapperAndService(handler = RpmExceptionHandler.class, mapper = ResumeMapper.class, minMapper = ResumeMapper.class, service = ResumeService.class)
public class ResumeController extends MappedCrudController<Long, Resume, ResumeDto, ResumeDto, ResumeService> {

    @Autowired
    private ResumeSharedInfoRepository resumeSharedInfoRepository;
    @Autowired
    private ResumeShareInfoMapper resumeShareInfoMapper;

    /**
     * Share resume response entity.
     *
     * @param getRequestContextService().getCurrentContext()        the request context
     * @param id                    the id
     * @param shareResumeRequestDto the share resume request dto
     * @return the response entity
     */
    @Operation(summary = "Share resume",
            description = "This endpoint shares a resume with multiple accounts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Resume shared successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumeShareInfoDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping(path = "/share/{id}")
    public ResponseEntity<List<ResumeShareInfoDto>> shareResume(
                                                                @PathVariable(name = RestApiConstants.ID) Long id,
                                                                @Valid @RequestBody ShareResumeRequestDto shareResumeRequestDto) {
        log.info("share resume ");
        try {
            return ResponseFactory.responseOk(resumeShareInfoMapper.listEntityToDto(crudService().shareWithAccounts(
                    id, shareResumeRequestDto.getResumeOwner(), shareResumeRequestDto.getAccountsCode())));
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }

    @Override
    public List<ResumeDto> afterFindAll(ContextRequestDto requestContext, List<ResumeDto> resumeDtos) {
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
     * @param getRequestContextService().getCurrentContext()  the request context
     * @param id              the id
     * @param resumeShareInfo the resume share info
     * @return the response entity
     */
    @Operation(summary = "Update resume review",
            description = "This endpoint updates the review information for a shared resume.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Resume review updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumeShareInfo.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid review data provided"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PutMapping(path = "/resume-review/update/{id}")
    public ResponseEntity<ResumeShareInfo> updateResumeReview(
                                                              @PathVariable(name = RestApiConstants.ID) Long id,
                                                              @Valid @RequestBody ResumeShareInfo resumeShareInfo) {
        log.info("update resume review");
        try {
            ResumeShareInfo resumeDto = resumeSharedInfoRepository.save(resumeShareInfo);
            return ResponseFactory.responseOk(resumeDto);
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Gets resume by account code.
     
     * @return the resume by account code
     */
    @Operation(summary = "Get resume by candidate code",
            description = "This endpoint retrieves the resume associated with the current user (candidate).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Resume retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumeDto.class))}),
            @ApiResponse(responseCode = "204",
                    description = "No resume found for the current candidate"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping(path = "/findByCandidateCode")
    public ResponseEntity<ResumeDto> getResumeByAccountCode() {
        try {
            ResumeDto resumeDto = mapper().entityToDto(crudService().getResumeByAccountCode(getRequestContextService().getCurrentContext().getSenderUser()));
            if (resumeDto != null) {
                return ResponseFactory.responseOk(resumeDto);
            }
            return ResponseEntity.noContent().build();
        } catch (Throwable e) {
            log.error(CtrlConstants.ERROR_API_EXCEPTION, e);
            return getBackExceptionResponse(e);
        }
    }


    /**
     * Create current user resume response entity.
     
     * @param resumeDto      the resume dto
     * @return the response entity
     */
    @Operation(summary = "Create current user resume",
            description = "This endpoint creates a new resume for the current user (candidate).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Resume created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResumeDto.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid resume data provided"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping(path = "/candidate")
    public ResponseEntity<ResumeDto> createCurrentUserResume(
                                                             @Valid @RequestBody ResumeDto resumeDto) {
        try {
            if (!StringUtils.hasText(resumeDto.getTenant())) {
                resumeDto.setTenant(getRequestContextService().getCurrentContext().getSenderTenant());
            }
            return ResponseFactory.responseOk(mapper().entityToDto(crudService().createResumeForAccount(getRequestContextService().getCurrentContext().getSenderUser(),
                    mapper().dtoToEntity(resumeDto))));
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }

    /**
     * Assign account to resume response entity.
     
     * @param resumeDto      the resume dto
     * @return the response entity
     */
    @Operation(summary = "Assign account to resume",
            description = "This endpoint assigns an account code to a specific resume.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Account assigned successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request data"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping(path = "/create/account")
    public ResponseEntity<Boolean> assignAccountToResume(
            
            @Valid @RequestBody ResumeDto resumeDto) {
        try {
            crudService().createAccount(mapper().dtoToEntity(resumeDto));
            return ResponseFactory.responseOk(Boolean.TRUE);
        } catch (Exception ex) {
            return getBackExceptionResponse(ex);
        }
    }
}
