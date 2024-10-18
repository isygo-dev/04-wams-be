package eu.isygoit.dto.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * The type Job offer global stat dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class JobOfferGlobalStatDto implements Serializable {

    private Long totalCount;
    private Long activeCount; // deadline not reached
    private Long confirmedCount; // nbre de position atteint
    private Long expiredCount;
}
