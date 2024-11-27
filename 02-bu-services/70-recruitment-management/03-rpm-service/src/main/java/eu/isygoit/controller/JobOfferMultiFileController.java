package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.api.IMappedMultiFileUploadApi;
import eu.isygoit.com.rest.controller.impl.MappedMultiFileController;
import eu.isygoit.dto.data.JobOfferDto;
import eu.isygoit.dto.data.JobOfferLinkedFileDto;
import eu.isygoit.exception.handler.RpmExceptionHandler;
import eu.isygoit.mapper.EntityMapper;
import eu.isygoit.mapper.JobOfferLinkedFileMapper;
import eu.isygoit.mapper.JobOfferMapper;
import eu.isygoit.model.JobOffer;
import eu.isygoit.service.impl.JobOfferMultiFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Job offer multi file controller.
 */
@Slf4j
@Validated
@RestController
@CtrlDef(handler = RpmExceptionHandler.class, mapper = JobOfferMapper.class, minMapper = JobOfferMapper.class, service = JobOfferMultiFileService.class)
@RequestMapping(path = "/api/v1/private/JobOffer")
public class JobOfferMultiFileController extends MappedMultiFileController<Long, JobOffer, JobOfferLinkedFileDto, JobOfferDto, JobOfferDto, JobOfferMultiFileService>
        implements IMappedMultiFileUploadApi<JobOfferLinkedFileDto, Long> {

    @Autowired
    private JobOfferLinkedFileMapper jobLinkedFileMapper;

    @Override
    public EntityMapper linkedFileMapper() {
        return jobLinkedFileMapper;
    }
}
