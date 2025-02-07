package eu.isygoit.service.impl;

import eu.isygoit.com.rest.service.impl.FileService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.model.Template;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.ITemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class TemplateService  extends FileService<Long, Template, TemplateRepository> implements ITemplateService {
    private final AppProperties appProperties;

    @Override
    protected String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();    }
}
