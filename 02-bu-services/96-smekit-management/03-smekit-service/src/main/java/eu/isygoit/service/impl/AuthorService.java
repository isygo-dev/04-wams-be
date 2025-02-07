package eu.isygoit.service.impl;

import eu.isygoit.com.rest.service.impl.CodifiableService;
import eu.isygoit.model.Author;
import eu.isygoit.repository.AuthorRepository;
import eu.isygoit.service.IAutherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorService extends CodifiableService<Long, Author, AuthorRepository> implements IAutherService {
}
