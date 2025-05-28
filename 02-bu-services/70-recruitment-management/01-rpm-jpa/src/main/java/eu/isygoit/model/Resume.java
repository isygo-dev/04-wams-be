package eu.isygoit.model;

import eu.isygoit.annotation.Criteria;
import eu.isygoit.constants.DomainConstants;
import eu.isygoit.converter.CamelCaseConverter;
import eu.isygoit.converter.LowerCaseConverter;
import eu.isygoit.listener.TimeLineListener;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

/**
 * The type Resume.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_RESUME,
        uniqueConstraints = {
                @UniqueConstraint(name = SchemaUcConstantName.UC_RESUME_CODE,
                        columnNames = {SchemaColumnConstantName.C_CODE}),
                @UniqueConstraint(name = SchemaUcConstantName.UC_RESUME_DOMAIN_EMAIL,
                        columnNames = {SchemaColumnConstantName.C_DOMAIN, SchemaColumnConstantName.C_EMAIL})
        })
@SecondaryTable(name = SchemaTableConstantName.T_RESUME_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID,
                referencedColumnName = SchemaColumnConstantName.C_ID)
)
@EntityListeners(TimeLineListener.class)
@SQLDelete(sql = "update " + SchemaTableConstantName.T_RESUME + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
@FilterDef(
        name = "firstNameFilter",
        parameters = {
                @ParamDef(name = "domain", type = String.class),
                @ParamDef(name = "firstName", type = String.class)
        }
)
@Filter(
        name = "firstNameFilter",
        condition = SchemaColumnConstantName.C_DOMAIN + "= :domain and " + SchemaColumnConstantName.C_FIRST_NAME + "= :firstName"
)
public class Resume extends AuditableCancelableEntity<Long>
        implements IDomainAssignable, ICodeAssignable, ITLEntity, IFileEntity, IMultiFileEntity<ResumeLinkedFile>, IImageEntity {

    @Id
    @SequenceGenerator(name = "resume_sequence_generator", sequenceName = "resume_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resume_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    private Long id;

    @Convert(converter = LowerCaseConverter.class)
    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;

    @Convert(converter = LowerCaseConverter.class)
    @Column(name = SchemaColumnConstantName.C_CODE, length = SchemaConstantSize.CODE, unique = true, updatable = false, nullable = false)
    private String code;

    @Convert(converter = CamelCaseConverter.class)
    @Criteria
    @Column(name = SchemaColumnConstantName.C_FIRST_NAME, length = SchemaConstantSize.S_NAME, nullable = false)
    private String firstName;

    @Convert(converter = CamelCaseConverter.class)
    @Criteria
    @Column(name = SchemaColumnConstantName.C_LAST_NAME, length = SchemaConstantSize.S_NAME, nullable = false)
    private String lastName;

    @Column(name = SchemaColumnConstantName.C_NATIONALITY, length = SchemaConstantSize.NATIONALITY)
    private String nationality;
    @Criteria
    @Column(name = SchemaColumnConstantName.C_BIRTHDAY)
    private LocalDate birthDate;
    @Criteria
    @Column(name = SchemaColumnConstantName.C_EMAIL, length = SchemaConstantSize.EMAIL, nullable = false)
    @Email(message = "email.should.be.valid")
    private String email;
    @Criteria
    @Column(name = SchemaColumnConstantName.C_PHONE_NUMBER, length = SchemaConstantSize.PHONE_NUMBER, nullable = false)
    private String phone;
    @Column(name = SchemaColumnConstantName.C_PHOTO)
    private String imagePath;
    @Column(name = SchemaColumnConstantName.C_SOURCE)
    private String source;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_ADDRESS, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_RESUME_REF_ADDRESS))
    private ResumeAddress address;
    @Criteria
    @Column(name = SchemaColumnConstantName.C_TITLE, length = ComSchemaConstantSize.S_NAME)
    private String title;
    @Column(name = SchemaColumnConstantName.C_PRESENTATION, length = ComSchemaConstantSize.XXL_DESCRIPTION)
    private String presentation;
    @Builder.Default
    @ColumnDefault("'true'")
    @Column(name = SchemaColumnConstantName.C_IS_LINKED_USER)
    private Boolean isLinkedToUser = Boolean.TRUE;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL  /* CASCADE only for OneToOne*/)
    @JoinColumn(name = SchemaColumnConstantName.C_RESUME_DTAILS, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_RESUME_REF_DETAILS))
    private ResumeDetails details;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL /* Cascade only for OneToMany*/)
    @JoinColumn(name = SchemaColumnConstantName.C_RESUME, referencedColumnName = SchemaColumnConstantName.C_CODE
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_SHARED_WITH_REF_RESUME))
    private List<ResumeShareInfo> resumeShareInfos;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_RESUME, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_ADDITIONAL_FILE_REF_RESUME))
    private List<ResumeLinkedFile> additionalFiles;

    //BEGIN IFileEntity : SecondaryTable / ResumeFile
    @Column(name = SchemaColumnConstantName.C_FILE_NAME, table = SchemaTableConstantName.T_RESUME_FILE)
    private String fileName;
    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_RESUME_FILE)
    private String originalFileName;
    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_PATH, table = SchemaTableConstantName.T_RESUME_FILE)
    private String path;
    @Column(name = SchemaColumnConstantName.C_EXTENSION, table = SchemaTableConstantName.T_RESUME_FILE)
    private String extension;
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_RESUME_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_RESUME_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_RESUME,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAG_REF_RESUME_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
    //END IFileEntity : SecondaryTable

    /**
     * Gets full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return new StringBuilder()
                .append(this.getFirstName())
                .append(" ")
                .append(this.getLastName())
                .toString();
    }
}
