package eu.isygoit.dto.data;


import eu.isygoit.dto.extendable.AuditableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Job template dto.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferTemplateDto extends AuditableDto<Long> {

    private String tenant;
    private String title;
    private JobOfferDto jobOffer;

}
