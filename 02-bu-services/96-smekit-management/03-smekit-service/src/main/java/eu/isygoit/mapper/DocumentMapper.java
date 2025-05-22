package eu.isygoit.mapper;

import eu.isygoit.dto.data.DocumentDto;
import eu.isygoit.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface DocumentMapper extends EntityMapper<Document, DocumentDto> {
}
