package eu.isygoit.mapper;

import eu.isygoit.dto.data.SharedWithDto;
import eu.isygoit.dto.data.TagDto;
import eu.isygoit.model.SharedWith;
import eu.isygoit.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")

public interface TagMapped extends EntityMapper<Tag, TagDto>{
}
