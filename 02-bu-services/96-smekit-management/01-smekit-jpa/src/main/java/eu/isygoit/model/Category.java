package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.enums.IEnumCategoryStatus;
import eu.isygoit.model.jakarta.AuditableEntity;
import eu.isygoit.model.schema.SchemaColumnConstantName;
import eu.isygoit.model.schema.SchemaConstantSize;
import eu.isygoit.model.schema.SchemaTableConstantName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = SchemaTableConstantName.T_CATEGORY)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category extends AuditableEntity<Long> implements IDomainAssignable, IImageEntity {
    @Id
    @SequenceGenerator(name = "category_sequence_generator", sequenceName = "category_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.S_NAME, updatable = false, nullable = false)
    private String domain;

    @Column(name = SchemaColumnConstantName.C_NAME, length = SchemaConstantSize.S_NAME, nullable = false)
    private String name;

    @Column(name = SchemaColumnConstantName.C_DESCRIPTION, length = SchemaConstantSize.DESCRIPTION)
    private String description;


    @Column(name = SchemaColumnConstantName.C_PHOTO)
    private String imagePath;


    @Builder.Default
    @ColumnDefault("'DISABLED'")
    @Enumerated(EnumType.STRING)
    @Column(name = SchemaColumnConstantName.C_STATUS, length = IEnumCategoryStatus.STR_ENUM_SIZE, nullable = false)
    private IEnumCategoryStatus.Status type = IEnumCategoryStatus.Status.DISABLED;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "category_tag",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name =SchemaColumnConstantName.C_CAT , referencedColumnName = SchemaColumnConstantName.C_ID,
//            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TEMPLATE_CATEGORY))
//    private List<Template> templates ;


}
