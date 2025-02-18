package eu.isygoit.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class SharedWithDto extends AbstractAuditableDto <Long>{
    private  String userName;
    private  String documentCode;

    @JsonIgnore
    private DocumentDto document ;
}
