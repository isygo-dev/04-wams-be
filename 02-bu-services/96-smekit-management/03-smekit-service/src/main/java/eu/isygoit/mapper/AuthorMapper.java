package eu.isygoit.mapper;

import eu.isygoit.dto.data.AuthorDto;
import eu.isygoit.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, componentModel = "spring")

public interface AuthorMapper extends EntityMapper<Author, AuthorDto>{




}
