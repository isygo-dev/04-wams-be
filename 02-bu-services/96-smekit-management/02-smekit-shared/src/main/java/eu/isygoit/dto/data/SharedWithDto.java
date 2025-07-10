package eu.isygoit.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.isygoit.dto.extendable.AbstractAuditableDto;
import eu.isygoit.enums.IEnumDocCommentsStaus;
import eu.isygoit.enums.IEnumPermissionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class SharedWithDto extends AbstractAuditableDto <Long>{
    private String user;
    private  String documentCode;
    private IEnumPermissionLevel.PermissionLevel permission;
    private DocumentDto document ;


}
