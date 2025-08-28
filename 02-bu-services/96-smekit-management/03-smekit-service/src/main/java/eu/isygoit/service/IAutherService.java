package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethods;
import eu.isygoit.model.Author;

public interface IAutherService extends ICrudServiceMethods<Long, Author> {

    String getUploadDirectory();
}
