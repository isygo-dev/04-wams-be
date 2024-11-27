package eu.isygoit.dto.data;


import eu.isygoit.dto.extendable.AbstractAuditableDto;
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
public class JobOfferTemplateDto extends AbstractAuditableDto<Long> {

    private String domain;
    private String title;
    private JobOfferDto jobOffer;

}
