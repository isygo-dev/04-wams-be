package eu.isygoit.service.impl;

import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.SharedWith;
import eu.isygoit.repository.SharedWithRepository;
import eu.isygoit.service.ISharedWithService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SharedWithService extends CrudService <Long, SharedWith, SharedWithRepository> implements ISharedWithService {
}
