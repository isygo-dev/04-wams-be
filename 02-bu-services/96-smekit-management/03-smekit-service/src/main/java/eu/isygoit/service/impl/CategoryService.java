package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.Category;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = CategoryRepository.class)
public class CategoryService extends CrudService <Long, Category, CategoryRepository>  implements ICategoryService {
}
