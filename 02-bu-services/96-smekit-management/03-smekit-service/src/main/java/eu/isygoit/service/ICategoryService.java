package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceOperations;
import eu.isygoit.model.Category;

import java.util.Map;

public interface ICategoryService extends ICrudServiceOperations<Long, Category> {

    int countTemplatesForCategory(Long categoryId);

    Map<Long, Integer> countTemplatesByCategory();
}
