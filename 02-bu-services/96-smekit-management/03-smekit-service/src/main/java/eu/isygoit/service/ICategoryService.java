package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethods;
import eu.isygoit.model.Category;

import java.util.Map;

public interface ICategoryService extends ICrudServiceMethods<Long, Category> {

    int countTemplatesForCategory(Long categoryId);

    Map<Long, Integer> countTemplatesByCategory();
}
