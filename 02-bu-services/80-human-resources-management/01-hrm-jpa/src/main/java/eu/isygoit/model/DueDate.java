package eu.isygoit.model;

import eu.isygoit.model.schema.SchemaColumnConstantName;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

/**
 * The type Due date.
 */
@Embeddable
@Data
public class DueDate {


    @Column(name = SchemaColumnConstantName.C_AMOUNT)
    private Double amount;
    @Column(name = SchemaColumnConstantName.C_PAYMENT_DATE)
    private LocalDate paymentDate;
    @Column(name = SchemaColumnConstantName.C_IS_PAYED)
    private Boolean isPayed;

}
