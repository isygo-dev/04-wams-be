package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.model.Category;

import java.util.Map;

public interface ICategoryService extends ICrudServiceMethod<Long, Category> {
    int countTemplatesForCategory(Long categoryId) ;
    Map<Long, Integer> countTemplatesByCategory();
}
