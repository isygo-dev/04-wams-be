package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceOperations;
import eu.isygoit.model.Author;

public interface IAutherService extends ICrudServiceOperations<Long, Author> {

    String getUploadDirectory();
}
