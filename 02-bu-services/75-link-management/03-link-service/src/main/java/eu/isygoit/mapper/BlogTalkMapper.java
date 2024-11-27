package eu.isygoit.mapper;

import eu.isygoit.dto.data.BlogTalkDto;
import eu.isygoit.model.BlogTalk;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

/**
 * The interface Blog talk mapper.
 */
@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")
public interface BlogTalkMapper extends EntityMapper<BlogTalk, BlogTalkDto> {
}
