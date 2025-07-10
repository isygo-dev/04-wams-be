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

}
