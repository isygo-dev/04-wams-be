package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.api.IUploadMultiFileApi;
import eu.isygoit.com.rest.controller.impl.MappedMultiFileController;
import eu.isygoit.dto.data.ResumeDto;
import eu.isygoit.dto.data.ResumeLinkedFileDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.EntityMapper;
import eu.isygoit.mapper.ResumeLinkedFileMapper;
import eu.isygoit.mapper.ResumeMapper;
import eu.isygoit.model.Resume;
import eu.isygoit.service.impl.ResumeMultiFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Resume multi file controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = RpmExceptionHandler.class, mapper = ResumeMapper.class, minMapper = ResumeMapper.class, service = ResumeMultiFileService.class)
@RequestMapping(path = "/api/v1/private/resume")
public class ResumeMultiFileController extends MappedMultiFileController<Long, Resume, ResumeLinkedFileDto, ResumeDto, ResumeDto, ResumeMultiFileService>
        implements IUploadMultiFileApi<ResumeLinkedFileDto, Long> {

    @Autowired
    private ResumeLinkedFileMapper resumeLinkedFileMapper;

    @Override
    public EntityMapper linkedFileMapper() {
        return resumeLinkedFileMapper;
    }
}
