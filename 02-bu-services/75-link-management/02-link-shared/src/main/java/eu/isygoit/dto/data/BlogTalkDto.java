package eu.isygoit.dto.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.isygoit.deserializer.UuidDeserializer;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


/**
 * The type Blog talk dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BlogTalkDto extends AbstractAuditableDto<UUID> {

    @JsonDeserialize(using = UuidDeserializer.class)
    private UUID id;
    private Long blogId;
    private String text;
    @JsonDeserialize(using = UuidDeserializer.class)
    private UUID parent;
    private String accountCode;
}
