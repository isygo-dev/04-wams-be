package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.model.Document;
import eu.isygoit.model.Quiz;

import java.io.IOException;
import java.util.List;

public interface IDocumentService extends ICrudServiceMethod<Long, Document> {
    Document createFromTemplate(Long templateId, String content , String name) throws IOException;
}
