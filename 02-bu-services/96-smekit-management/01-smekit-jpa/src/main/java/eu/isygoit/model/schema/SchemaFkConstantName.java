package eu.isygoit.model.schema;

/**
 * The interface Schema fk constant name.
 */
public interface SchemaFkConstantName extends ComSchemaFkConstantName {


    String FK_TEMPLATE_REF_CATEGORY = "fk_template_category";
    String FK_TEMPLATE_REF_AUTHOR = "FK_TEMPLATE_REF_AUTHOR";
    String FK_TAG_REF_AUTHOR_FILE = "FK_TAG_REF_AUTHOR_FILE";
    String FK_FAVORITE_REF_TEMPLATE = "FK_FAVORITE_REF_TEMPLATE";
    String FK_TAG_REF_CATEGORY = "FK_TAG_REF_CATEGORY";
    String FK_TAG_REF_TEMPLATE = "FK_TAG_REF_TEMPLATE";
    String FK_SHARED_WITH_REF_DOCUMENT = "FK_SHARED_WITH_REF_DOCUMENT";
    String FK_COMMENT_REF_DOCUMENT = "FK_COMMENT_REF_DOCUMENT";
    String FK_DOCUMENT_REF_TEMPLATE = "FK_DOCUMENT_REF_TEMPLATE";
}
