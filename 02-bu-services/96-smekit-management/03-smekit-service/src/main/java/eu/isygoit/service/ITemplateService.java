package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceOperations;
import eu.isygoit.com.rest.service.media.IFileServiceOperations;
import eu.isygoit.model.Template;

import java.util.List;

public interface ITemplateService extends ICrudServiceOperations<Long, Template>, IFileServiceOperations<Long, Template> {

    List<Template> findAllByFavoritesContaining(String userName);

    Template findAllByIdAndFavoritesContaining(Long id, String userName);

    Long countTemplatesWithFavoriteUser(String userName);
}
