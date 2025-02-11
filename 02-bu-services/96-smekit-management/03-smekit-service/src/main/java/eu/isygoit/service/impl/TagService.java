package eu.isygoit.service.impl;

import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.Tag;
import eu.isygoit.repository.TagRepository;
import eu.isygoit.service.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagService extends CrudService<Long, Tag, TagRepository> implements ITagService {
}
