package eu.isygoit.mapper;

import eu.isygoit.dto.data.DocumentCommentDto;
import eu.isygoit.model.DocumentComment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface DocumentCommentMapper extends EntityMapper<DocumentComment, DocumentCommentDto> {
}
