package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.CrudService;
import eu.isygoit.model.Tag;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.TagRepository;
import eu.isygoit.service.ITagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = TagRepository.class)
public class TagService extends CrudService<Long, Tag, TagRepository> implements ITagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        super();
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Tag findOrCreateTag(String tagName) {
        if (tagName == null || tagName.trim().isEmpty()) {
            return null;
        }

        String normalizedTagName = tagName.trim().toLowerCase();
        log.debug("Looking for tag with name: {}", normalizedTagName);

        Optional<Tag> existingTag = tagRepository.findByTagName(normalizedTagName);
        if (existingTag.isPresent()) {
            log.debug("Found existing tag: {}", existingTag.get().getId());
            return existingTag.get();
        } else {
            log.debug("Creating new tag: {}", normalizedTagName);
            Tag newTag = new Tag();
            newTag.setTagName(normalizedTagName);
            newTag.setCategories(new HashSet<>());
            return tagRepository.save(newTag);
        }
    }

    @Override
    @Transactional
    public Set<Tag> processTagNames(Set<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return new HashSet<>();
        }

        log.debug("Processing tag names: {}", String.join(", ", tagNames));

        Set<Tag> processedTags = tagNames.stream()
                .filter(tagName -> tagName != null && !tagName.trim().isEmpty())
                .map(this::findOrCreateTag)
                .collect(Collectors.toSet());

        log.debug("Processed {} tags", processedTags.size());
        return processedTags;
    }
}