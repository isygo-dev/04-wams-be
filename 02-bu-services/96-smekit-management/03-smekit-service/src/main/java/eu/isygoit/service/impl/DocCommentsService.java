package eu.isygoit.service.impl;

import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.DocComment;
import eu.isygoit.model.TempCategory;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.repository.DocCommentsRepository;
import eu.isygoit.service.IDocCommentsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DocCommentsService extends CrudService<Long, DocComment, DocCommentsRepository> implements IDocCommentsService {
}
