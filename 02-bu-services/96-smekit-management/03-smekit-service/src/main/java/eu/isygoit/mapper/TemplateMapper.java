package eu.isygoit.mapper;

import eu.isygoit.dto.data.QuizDto;
import eu.isygoit.dto.data.TemplateDto;
import eu.isygoit.model.Quiz;
import eu.isygoit.model.Template;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")

public interface TemplateMapper extends EntityMapper<Template, TemplateDto> {
}
