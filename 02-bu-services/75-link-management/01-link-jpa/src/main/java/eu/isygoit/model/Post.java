package eu.isygoit.model;

import eu.isygoit.constants.DomainConstants;
import eu.isygoit.model.jakarta.AuditableCancelableEntity;
import eu.isygoit.model.schema.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

/**
 * The type Post.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SchemaTableConstantName.T_POST)
@SecondaryTable(name = SchemaTableConstantName.T_POST_FILE,
        pkJoinColumns = @PrimaryKeyJoinColumn(name = SchemaColumnConstantName.C_ID,
                referencedColumnName = SchemaColumnConstantName.C_ID)
)
@SQLDelete(sql = "update " + SchemaTableConstantName.T_POST + " set " + SchemaColumnConstantName.C_CHECK_CANCEL + "= true , " + ComSchemaColumnConstantName.C_CANCEL_DATE + " = current_timestamp WHERE id = ?")
@Where(clause = SchemaColumnConstantName.C_CHECK_CANCEL + "=false")
public class Post extends AuditableCancelableEntity<Long>
        implements IDomainAssignable, IImageEntity, IFileEntity, ICodeAssignable {

    @Id
    @SequenceGenerator(name = "post_sequence_generator", sequenceName = "post_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence_generator")
    @Column(name = SchemaColumnConstantName.C_ID, updatable = false, nullable = false)
    @OrderBy(SchemaColumnConstantName.C_CREATE_DATE + " DESC")
    private Long id;

    @ColumnDefault("'" + DomainConstants.DEFAULT_DOMAIN_NAME + "'")
    @Column(name = SchemaColumnConstantName.C_DOMAIN, length = SchemaConstantSize.DOMAIN, updatable = false, nullable = false)
    private String domain;
    @Column(name = SchemaColumnConstantName.C_ACCOUNT, length = SchemaConstantSize.CODE, updatable = false, nullable = false)
    private String accountCode;
    @Column(name = ComSchemaColumnConstantName.C_CODE, length = ComSchemaConstantSize.CODE, updatable = false, nullable = false)
    private String code;
    @Column(name = SchemaColumnConstantName.C_TITLE, length = SchemaConstantSize.S_TITLE, nullable = false)
    private String title;
    @Column(name = SchemaColumnConstantName.C_TALK, length = SchemaConstantSize.XXL_DESCRIPTION, nullable = false)
    private String talk;
    @OrderBy(SchemaColumnConstantName.C_CREATE_DATE + " DESC")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = SchemaColumnConstantName.C_POST_ID, referencedColumnName = SchemaColumnConstantName.C_ID
            , foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_COMMENT_REF_POST))
    private List<PostComment> comments;
    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_POST_LIKED
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_POST_ID,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_POST_REF_ACCOUNT)))
    @Column(name = SchemaColumnConstantName.C_ACCOUNT_CODE)
    private List<String> usersAccountCode;
    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_POST_DISLIKED
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_POST_ID,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_POST_REF_ACCOUNT)))
    @Column(name = SchemaColumnConstantName.C_ACCOUNT_CODE_DISLIKE)
    private List<String> usersAccountCodeDislike;
    @Column(name = SchemaColumnConstantName.C_PHOTO)
    private String imagePath;

    @Column(name = SchemaColumnConstantName.C_IS_BLOG)
    private Boolean isBlog;

    //BEGIN IFileEntity : SecondaryTable / PostFile
    @Column(name = SchemaColumnConstantName.C_FILE_NAME, table = SchemaTableConstantName.T_POST_FILE)
    private String fileName;
    @Column(name = SchemaColumnConstantName.C_ORIGINAL_FILE_NAME, table = SchemaTableConstantName.T_POST_FILE)
    private String originalFileName;
    @ColumnDefault("'NA'")
    @Column(name = SchemaColumnConstantName.C_PATH, table = SchemaTableConstantName.T_POST_FILE)
    private String path;
    @Column(name = SchemaColumnConstantName.C_EXTENSION, table = SchemaTableConstantName.T_POST_FILE)
    private String extension;
    @Column(name = SchemaColumnConstantName.C_TYPE, table = SchemaTableConstantName.T_POST_FILE)
    private String type;

    @ElementCollection
    @CollectionTable(name = SchemaTableConstantName.T_POST_FILE_TAGS
            , joinColumns = @JoinColumn(name = SchemaColumnConstantName.C_POST,
            referencedColumnName = SchemaColumnConstantName.C_ID,
            foreignKey = @ForeignKey(name = SchemaFkConstantName.FK_TAGS_REF_POST_FILE)))
    @Column(name = SchemaColumnConstantName.C_TAG_OWNER)
    private List<String> tags;
    //END IFileEntity : SecondaryTable
}
