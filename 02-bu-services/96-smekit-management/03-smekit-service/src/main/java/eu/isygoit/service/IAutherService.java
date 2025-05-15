package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.model.Author;
import eu.isygoit.model.Quiz;

public interface IAutherService extends ICrudServiceMethod<Long, Author> {
    String getUploadDirectory();
}
