package eu.isygoit.service.impl;

import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.controller.TempCategoryRestController;
import eu.isygoit.model.TempCategory;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.service.ICategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryService extends CrudService <Long, TempCategory, CategoryRepository>  implements ICategoryService {
}
