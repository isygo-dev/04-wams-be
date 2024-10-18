package eu.isygoit.model;

import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * The type Emergency contact.
 */
@Embeddable
@Data
public class EmergencyContact {

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String name;
    @Column(name = SchemaColumnConstantName.C_RELATION, length = SchemaConstantSize.RELATION)
    private String relation;

    @Column(name = SchemaColumnConstantName.C_PHONE_NUMBER, length = SchemaConstantSize.PHONE_NUMBER)
    private String phoneNumber;
}
