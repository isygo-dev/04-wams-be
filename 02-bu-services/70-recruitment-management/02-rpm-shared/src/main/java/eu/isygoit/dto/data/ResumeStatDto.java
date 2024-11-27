package eu.isygoit.dto.data;


import eu.isygoit.dto.extendable.IdentifiableDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * The type Resume stat dto.
 */
@Data
@AllArgsConstructor
@SuperBuilder
public class ResumeStatDto extends IdentifiableDto<Long> {

    // Pourcentage completion
    private Long completion;
    // nbre de test passe
    private Long realizedTestsCount;
    // nbre de job application
    private Long applicationsCount;
    // nbre d'application active
    private Long ongoingApplicationsCount;
    // nbre d'event de type
    private Long interviewCount;
}
