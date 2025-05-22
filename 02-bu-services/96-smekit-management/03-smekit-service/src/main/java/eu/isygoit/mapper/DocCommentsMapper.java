package eu.isygoit.mapper;

import eu.isygoit.dto.data.DocCommentDto;
import eu.isygoit.model.DocComment;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface DocCommentsMapper extends EntityMapper<DocComment, DocCommentDto> {
}
