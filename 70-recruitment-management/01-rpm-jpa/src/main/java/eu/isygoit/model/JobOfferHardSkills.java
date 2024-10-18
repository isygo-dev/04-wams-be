package eu.isygoit.model;

import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The type Job offer hard skills.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_JOB_OFFER_SKILLS)
@DiscriminatorValue("HARD")
public class JobOfferHardSkills extends JobOfferSkills {

}