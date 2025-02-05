package eu.isygoit.async.camel.processor;

import eu.isygoit.com.camel.processor.AbstractStringProcessor;
import eu.isygoit.dto.data.AssoAccountDto;
import eu.isygoit.exception.OperationNotAllowedException;
import eu.isygoit.exception.ResumeNotFoundException;
import eu.isygoit.helper.JsonHelper;
import eu.isygoit.model.AssoAccountResume;
import eu.isygoit.repository.AssoAccountResumeRepository;
import eu.isygoit.service.IResumeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * The type Asso account resume processor.
 */
@Slf4j
@Component
@Qualifier("assoAccountResumeProcessor")
public class AssoAccountResumeProcessor extends AbstractStringProcessor {
    /**
     * The Resume service.
     */
    @Autowired
    IResumeService resumeService;
    /**
     * The Asso account resume repository.
     */
    @Autowired
    AssoAccountResumeRepository assoAccountResumeRepository;

    @Override
    public void performProcessor(Exchange exchange, String object) throws Exception {
        AssoAccountDto assoAccountDto = JsonHelper.fromJson(object, AssoAccountDto.class);
        String[] splitOrigin = assoAccountDto.getOrigin().split("-");
        resumeService.findByCode(splitOrigin[1])
                .ifPresentOrElse(resume -> {
                            if ("RESUME".equals(splitOrigin[0])) {
                                assoAccountResumeRepository.save(AssoAccountResume.builder().accountCode(assoAccountDto.getCode()).resume(resume).build());
                            } else {
                                throw new OperationNotAllowedException("asso account resume source " + splitOrigin[0]);
                            }
                        },
                        () -> {
                            throw new ResumeNotFoundException("with code " + splitOrigin[1]);
                        });
    }
}
