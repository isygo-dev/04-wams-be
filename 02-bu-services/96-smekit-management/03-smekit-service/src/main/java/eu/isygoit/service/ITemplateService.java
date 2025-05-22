package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.model.Template;

public interface ITemplateService extends ICrudServiceMethod<Long, Template>, IFileServiceMethods<Long, Template> {
}
