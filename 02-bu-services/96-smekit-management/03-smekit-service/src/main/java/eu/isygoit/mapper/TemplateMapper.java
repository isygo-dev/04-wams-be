package eu.isygoit.mapper;

import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.model.Author;
import eu.isygoit.model.Category;
import eu.isygoit.model.Quiz;
import eu.isygoit.model.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Optional;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")

public interface TemplateMapper extends EntityMapper<Template, TemplateDto> {
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "categoryId", source = "category.id")
    TemplateDto entityToDto(Template template);

    @Mapping(target = "author", expression = "java(mapAuthor(dto.getAuthorId()))")
    @Mapping(target = "category", expression = "java(mapCategory(dto.getCategoryId()))")
    Template dtoToEntity(TemplateDto dto);

    default Author mapAuthor(Long authorId) {
        if (authorId == null) return null;
        Author author = new Author();
        author.setId(authorId);
        return author;
    }

    default Category mapCategory(Long categoryId) {
        if (categoryId == null) return null;
        Category
                category = new Category();
        category.setId(categoryId);
        return category;
    }

    TemplateDto entityToDto(Optional<Template> fullTemplate);
}
