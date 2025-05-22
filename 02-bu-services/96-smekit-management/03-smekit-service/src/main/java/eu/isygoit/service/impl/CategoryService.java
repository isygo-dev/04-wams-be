package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.ImageService;
import eu.isygoit.config.AppProperties;
import eu.isygoit.dto.data.CategoryDto;
import eu.isygoit.dto.data.TagDto;
import eu.isygoit.exception.ResourceNotFoundException;
import eu.isygoit.model.Category;
import eu.isygoit.model.Tag;
import eu.isygoit.remote.dms.DmsLinkedFileService;
import eu.isygoit.remote.kms.KmsIncrementalKeyService;
import eu.isygoit.repository.CategoryRepository;
import eu.isygoit.repository.TagRepository;
import eu.isygoit.repository.TemplateRepository;
import eu.isygoit.service.ICategoryService;
import eu.isygoit.service.ITagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@CodeGenLocal(value = NextCodeService.class)
@CodeGenKms(value = KmsIncrementalKeyService.class)
@SrvRepo(value = CategoryRepository.class)
@DmsLinkFileService(DmsLinkedFileService.class)
public class CategoryService extends ImageService<Long, Category, CategoryRepository> implements ICategoryService {

    private final AppProperties appProperties;
    private final ITagService tagService;
    private  final TemplateRepository templateRepository ;

    private  final  CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    public CategoryService(AppProperties appProperties, ITagService tagService, TemplateRepository templateRepository, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.appProperties = appProperties;
        this.tagService = tagService;
        this.templateRepository = templateRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public String getUploadDirectory() {
        return this.appProperties.getUploadDirectory();
    }

    @Override
    public Category create(Category entity) {
        processCategoryTags(entity);
        return super.create(entity);
    }

    @Override
    public Category update(Category entity) {
        processCategoryTags(entity);
        return super.update(entity);
    }

    private void processCategoryTags(Category category) {
        if (category.getTags() == null) {
            category.setTags(new HashSet<>());
            return;
        }

        Set<String> tagNames = category.getTags().stream()
                .map(Tag::getTagName)
                .filter(name -> name != null && !name.trim().isEmpty())
                .collect(Collectors.toSet());

        if (tagNames.isEmpty()) {
            category.setTags(new HashSet<>());
            return;
        }

        Set<Tag> processedTags = tagService.processTagNames(tagNames);

        for (Tag tag : processedTags) {
            tag.getCategories().add(category);
        }

        category.setTags(processedTags);

        log.debug("Tags processed for category {}: {}",
                category.getId() != null ? category.getId() : "new",
                processedTags.stream().map(Tag::getTagName).collect(Collectors.joining(", ")));
    }

    @Override
    public Optional<Category> findById(Long id) {
        Optional<Category> optional = super.findById(id);
        if (optional.isPresent() && optional.get().getTags() != null) {
            optional.get().getTags().size();
        }
        return optional;
    }

    public Category addTagsToCategory(Long categoryId, List<TagDto> tagDtos) {
        Optional<Category> optional = findById(categoryId);
        if(optional.isPresent()) {
            Set<Tag> existingTags = optional.get().getTags() != null ? optional.get().getTags() : new HashSet<>();

            for (TagDto tagDto : tagDtos) {
                Tag tag = (tagDto.getId() != null)
                        ? tagRepository.findById(tagDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagDto.getId()))
                        : tagService.findOrCreateTag(tagDto.getTagName());

                existingTags.add(tag);
                tag.getCategories().add(optional.get());
            }

            optional.get().setTags(existingTags);
            return repository().save(optional.get());
        }

        return null;
    }

    public Category setTagsForCategory(Long categoryId, List<TagDto> tagDtos) {
        Optional<Category> optional = findById(categoryId);
        if(optional.isPresent()) {
            Set<Tag> newTags = new HashSet<>();

            for (TagDto tagDto : tagDtos) {
                Tag tag = (tagDto.getId() != null)
                        ? tagRepository.findById(tagDto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagDto.getId()))
                        : tagService.findOrCreateTag(tagDto.getTagName());

                newTags.add(tag);
                tag.getCategories().add(optional.get());
            }

            if (optional.get().getTags() != null) {
                for (Tag oldTag : optional.get().getTags()) {
                    if (!newTags.contains(oldTag)) {
                        oldTag.getCategories().remove(optional.get());
                    }
                }
            }

            optional.get().setTags(newTags);
            return repository().save(optional.get());
        }

        return null;
    }

    public Category removeTagFromCategory(Long categoryId, Long tagId) {
        Optional<Category> optional = findById(categoryId);
        if(optional.isPresent()) {
            Set<Tag> existingTags = optional.get().getTags();

            if (existingTags != null) {
                Tag tagToRemove = existingTags.stream()
                        .filter(tag -> tag.getId().equals(tagId))
                        .findFirst()
                        .orElse(null);

                if (tagToRemove != null) {
                    existingTags.remove(tagToRemove);
                    tagToRemove.getCategories().remove(optional.get());
                    optional.get().setTags(existingTags);
                    repository().save(optional.get());
                }
            }

            return optional.get();
        }

        return null;
    }

    public Category updateFromDto(Long id, CategoryDto dto) {
        Optional<Category> optional = findById(id);
        if(optional.isPresent()) {
            if (dto.getName() != null) {
                optional.get().setName(dto.getName());
            }

            if (dto.getDescription() != null) {
                optional.get().setDescription(dto.getDescription());
            }

            if (dto.getTagName() != null && !dto.getTagName().isEmpty()) {
                Set<String> tagNames = dto.getTagName().stream()
                        .filter(tagDto -> tagDto != null)
                        .map(TagDto::getTagName)
                        .map(String::trim)
                        .filter(tag -> !tag.isEmpty())
                        .collect(Collectors.toSet());

                Set<Tag> processedTags = tagService.processTagNames(tagNames);

                for (Tag tag : processedTags) {
                    tag.getCategories().add(optional.get());
                }

                optional.get().setTags(processedTags);
            }
            return super.update(optional.get());
        }

        throw new ResourceNotFoundException("Category not found with id: " + id);
    }
    @Override
    public Map<Long, Integer> countTemplatesByCategory() {
        Map<Long, Integer> templateCountMap = templateRepository.countTemplatesByCategory().stream()
                .collect(Collectors.toMap(
                        result -> (Long) result[0],
                        result -> ((Long) result[1]).intValue()
                ));

        templateCountMap.put(null, templateRepository.countTemplatesWithoutCategory());

        return templateCountMap;
    }
    @Override
    public int countTemplatesForCategory(Long categoryId) {
        if (categoryId == null) {
            return (int) templateRepository.count();
        }
        return templateRepository.countByCategoryId(categoryId);
    }


}
