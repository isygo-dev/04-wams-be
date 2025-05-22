package eu.isygoit.service.impl;

import eu.isygoit.annotation.CodeGenKms;
import eu.isygoit.annotation.CodeGenLocal;
import eu.isygoit.annotation.DmsLinkFileService;
import eu.isygoit.annotation.SrvRepo;
import eu.isygoit.com.rest.service.impl.ImageService;
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
    public Category findById(Long id) {
        Category category = super.findById(id);
        if (category != null && category.getTags() != null) {
            category.getTags().size();
        }
        return category;
    }

    public Category addTagsToCategory(Long categoryId, List<TagDto> tagDtos) {
        Category category = findById(categoryId);
        Set<Tag> existingTags = category.getTags() != null ? category.getTags() : new HashSet<>();

        for (TagDto tagDto : tagDtos) {
            Tag tag = (tagDto.getId() != null)
                    ? tagRepository.findById(tagDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagDto.getId()))
                    : tagService.findOrCreateTag(tagDto.getTagName());

            existingTags.add(tag);
            tag.getCategories().add(category);
        }

        category.setTags(existingTags);
        return repository().save(category);
    }

    public Category setTagsForCategory(Long categoryId, List<TagDto> tagDtos) {
        Category category = findById(categoryId);
        Set<Tag> newTags = new HashSet<>();

        for (TagDto tagDto : tagDtos) {
            Tag tag = (tagDto.getId() != null)
                    ? tagRepository.findById(tagDto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagDto.getId()))
                    : tagService.findOrCreateTag(tagDto.getTagName());

            newTags.add(tag);
            tag.getCategories().add(category);
        }

        if (category.getTags() != null) {
            for (Tag oldTag : category.getTags()) {
                if (!newTags.contains(oldTag)) {
                    oldTag.getCategories().remove(category);
                }
            }
        }

        category.setTags(newTags);
        return repository().save(category);
    }

    public Category removeTagFromCategory(Long categoryId, Long tagId) {
        Category category = findById(categoryId);
        Set<Tag> existingTags = category.getTags();

        if (existingTags != null) {
            Tag tagToRemove = existingTags.stream()
                    .filter(tag -> tag.getId().equals(tagId))
                    .findFirst()
                    .orElse(null);

            if (tagToRemove != null) {
                existingTags.remove(tagToRemove);
                tagToRemove.getCategories().remove(category);
                category.setTags(existingTags);
                repository().save(category);
            }
        }

        return category;
    }

    public Category updateFromDto(Long id, CategoryDto dto) {
        Category existingCategory = findById(id);
        if (existingCategory == null) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        if (dto.getName() != null) {
            existingCategory.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            existingCategory.setDescription(dto.getDescription());
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
                tag.getCategories().add(existingCategory);
            }

            existingCategory.setTags(processedTags);
        }
        return super.update(existingCategory);
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
