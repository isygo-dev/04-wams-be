package eu.isygoit.dto.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * The type Resume global stat dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResumeGlobalStatDto implements Serializable {

    private Long totalCount;
    // nbre ceated by
    private Long uploadedByMeCount;
    private Long confirmedCount; // user has really loggedIn
    // porcentage Object cpmletetion
    private Long completedCount;
    // nbre d'interview global
    private Long interviewedCount;
}
