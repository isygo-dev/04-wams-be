package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.ComSchemaColumnConstantName;
import eu.isygoit.model.schema.ComSchemaConstantSize;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_TEMPLATE_FAVORITE")
public class TemplateFavorite extends AuditableEntity<Long>  implements ISAASEntity {
    @Id
    @SequenceGenerator(
            name = "templateFav_sequence_generator",
            sequenceName = "templateFav_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "templateFav_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Column(name = "USER_IDENTIFIER", nullable = false, length = 100)
    private String userIdentifier;

    @ManyToOne
//            (fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_FAVORITE_TEMPLATE"))
    private Template template;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = ComSchemaColumnConstantName.C_DOMAIN, length = ComSchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private  String domain;



}
