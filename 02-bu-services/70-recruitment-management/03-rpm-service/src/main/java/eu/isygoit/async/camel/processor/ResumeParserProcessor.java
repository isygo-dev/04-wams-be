package eu.isygoit.async.camel.processor;

import eu.isygoit.com.camel.processor.AbstractCamelProcessor;
import eu.isygoit.dto.data.ResumeParseDto;
import eu.isygoit.enums.IEnumFileType;
import eu.isygoit.enums.IEnumSkillLevelType;
import eu.isygoit.model.Resume;
import eu.isygoit.model.ResumeDetails;
import eu.isygoit.model.ResumeSkills;
import eu.isygoit.remote.dms.DmsFileFileConverterService;
import eu.isygoit.repository.ResumeRepository;
import eu.isygoit.repository.ResumeSkillsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The type Resume parser processor.
 */
@Slf4j
@Component
@Qualifier("resumeParserProcessor")
public class ResumeParserProcessor extends AbstractCamelProcessor<ResumeParseDto> {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeSkillsRepository resumeSkillsRepository;

    @Autowired
    private DmsFileFileConverterService dmsFileFileConverterService;

    @Override
    public void performProcessor(Exchange exchange, ResumeParseDto resumeParseDto) throws Exception {
        exchange.getIn().setHeader("domain", resumeParseDto.getDomain());
        exchange.getIn().setHeader("code", resumeParseDto.getCode());

        if (resumeParseDto.getFile() != null &&
                org.springframework.util.StringUtils.hasText(resumeParseDto.getFile().getOriginalFilename()) &&
                FilenameUtils.isExtension(resumeParseDto.getFile().getOriginalFilename().toLowerCase(), "pdf")) { //NOSONAR
            try {
                ResponseEntity<Resource> result = dmsFileFileConverterService.convertPdf(//RequestContextDto.builder().build(),
                        IEnumFileType.Types.TEXT, resumeParseDto.getFile());
                if (result.getStatusCode().is2xxSuccessful() && result.hasBody()) {
                    Optional<Resume> optional = resumeRepository.findByCodeIgnoreCase(resumeParseDto.getCode());
                    if (optional.isPresent()) {
                        Resume resume = optional.get();
                        try (Scanner scanner = new Scanner(result.getBody().getContentAsString(StandardCharsets.UTF_8))) {   //NOSONAR
                            List<String> tokens = scanner.tokens().collect(Collectors.toUnmodifiableList());
                            if (!CollectionUtils.isEmpty(tokens)) {
                                tokens.stream().forEach(s -> {
                                    //get email
                                    if (StringUtils.isEmpty(resume.getEmail()) && s.matches("([a-z0-9_.-]+)@([a-z0-9_.-]+[a-z])")) {
                                        resume.setEmail(s);
                                    } else
                                        //get phone number
                                        if (StringUtils.isEmpty(resume.getPhone()) && s.matches("^[+]+[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
                                            resume.setPhone(s);
                                        }
                                });
                            }

                            //Parse skills
                            List<String> skillNames = resumeSkillsRepository.findDistinctSkillNames();
                            if (!CollectionUtils.isEmpty(skillNames)) {
                                if (resume.getDetails() == null) {
                                    resume.setDetails(ResumeDetails.builder().skills(new ArrayList<>()).build());
                                }
                                if (resume.getDetails().getSkills() == null) {
                                    resume.getDetails().setSkills(new ArrayList<>());
                                }

                                skillNames.forEach(skillName -> {
                                    if (tokens.stream().filter(token -> token.equalsIgnoreCase(skillName.toLowerCase())).count() > 0) {
                                        if (resume.getDetails().getSkills().stream().
                                                filter(resumeSkills -> resumeSkills.getName().equalsIgnoreCase(skillName.toLowerCase())).count() == 0) {
                                            resume.getDetails().getSkills().add(ResumeSkills.builder()
                                                    .name(skillName)
                                                    .level(IEnumSkillLevelType.Types.BEGINNER)
                                                    .score(Math.floor(Math.random() * (100 - 10 + 1) + 10))
                                                    .build());
                                        }
                                    }
                                });
                            }
                        }
                        resumeRepository.save(resume);
                    }
                }
            } catch (Exception e) {
                log.error("Remote feign call failed : ", e);
                //throw new RemoteCallFailedException(e);
            }

        }

        exchange.getIn().setHeader(AbstractCamelProcessor.RETURN_HEADER, true);
    }
}
