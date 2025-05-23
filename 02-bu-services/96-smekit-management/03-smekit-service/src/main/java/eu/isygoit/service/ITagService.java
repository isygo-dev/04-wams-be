package eu.isygoit.service;

import eu.isygoit.com.rest.service.ICrudServiceMethod;
import eu.isygoit.model.Tag;

import java.util.Set;

public interface ITagService extends ICrudServiceMethod<Long, Tag> {
    Set<Tag> processTagNames(Set<String> tagNames);

    Tag findOrCreateTag(String tagName);

}
