package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.model.Author;

public interface IAutherService extends ICrudServiceMethod<Long, Author> {

    String getUploadDirectory();
}
