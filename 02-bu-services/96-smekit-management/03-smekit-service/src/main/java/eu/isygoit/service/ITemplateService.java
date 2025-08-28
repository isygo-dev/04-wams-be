package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethods;
import eu.isygoit.com.rest.service.IFileServiceMethods;
import eu.isygoit.model.Template;

import java.util.List;

public interface ITemplateService extends ICrudServiceMethods<Long, Template>, IFileServiceMethods<Long, Template> {

    List<Template> findAllByFavoritesContaining(String userName);

    Template findAllByIdAndFavoritesContaining(Long id, String userName);

    Long countTemplatesWithFavoriteUser(String userName);
}
