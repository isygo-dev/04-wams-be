package eu.isygoit.mapper;

import eu.isygoit.dto.data.CategoryDto;
import eu.isygoit.dto.data.TagDto;
import eu.isygoit.model.Category;
import eu.isygoit.model.Tag;
import eu.isygoit.repository.TagRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public abstract class CategoryMapper implements EntityMapper<Category, CategoryDto> {

    protected TagRepository tagRepository;

    @Autowired
    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Mapping(target = "tagName", source = "tags")
    public abstract CategoryDto entityToDto(Category entity);

    @Override
    @Mapping(target = "tags", source = "tagName")
    public abstract Category dtoToEntity(CategoryDto dto);

    protected List<TagDto> tagsToTagDtos(Set<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return new ArrayList<>();
        }
        return tags.stream()
                .map(this::mapToTagDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Set<Tag> tagDtosToTags(List<TagDto> tagDtos) {
        Set<Tag> tags = new HashSet<>();

        for (TagDto tagDto : tagDtos) {
            if (tagDto == null) continue;

            Tag tag = null;

            if (tagDto.getId() != null) {
                tag = tagRepository.findById(tagDto.getId()).orElse(null);
            }

            if (tag == null && tagDto.getTagName() != null && !tagDto.getTagName().trim().isEmpty()) {
                tag = tagRepository.findByTagName(tagDto.getTagName()).orElse(null);

                if (tag == null) {
                    tag = new Tag();
                    tag.setTagName(tagDto.getTagName());
                    tag = tagRepository.save(tag);
                }
            }

            if (tag != null) {
                tags.add(tag);
            }
        }

        tagRepository.flush();
        return tags;
    }

    private TagDto mapToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setTagName(tag.getTagName());
        tagDto.setCreateDate(tag.getCreateDate());
        tagDto.setCreatedBy(tag.getCreatedBy());
        tagDto.setUpdateDate(tag.getUpdateDate());
        tagDto.setUpdatedBy(tag.getUpdatedBy());
        return tagDto;
    }
}
