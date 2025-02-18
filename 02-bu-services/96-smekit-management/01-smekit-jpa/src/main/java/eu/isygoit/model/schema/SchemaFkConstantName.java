package eu.isygoit.model.schema;

/**
 * The interface Schema fk constant name.
 */
public interface SchemaFkConstantName extends ComSchemaFkConstantName {

    /**
     * The constant FK_ACCOUNT_REF_PASSWORD_INFO.
     */
    String FK_ACCOUNT_REF_PASSWORD_INFO = "FK_ACCOUNT_REF_PASSWORD_INFO";
    /**
     * The constant FK_SECTION_REF_QUIZ.
     */
    String FK_SECTION_REF_QUIZ = "FK_SECTION_REF_QUIZ";
    /**
     * The constant FK_CAND_QUIZ_REF_QUIZ.
     */
    String FK_CAND_QUIZ_REF_QUIZ = "FK_CAND_QUIZ_REF_QUIZ";
    /**
     * The constant FK_CAND_QUIZ_ANS_REF_CAND_QUIZ.
     */
    String FK_CAND_QUIZ_ANS_REF_CAND_QUIZ = "FK_CAND_QUIZ_ANS_REF_CAND_QUIZ";
    String FK_TEMPLATE_CATEGORY="fk_template_category";
    String FK_TEMPLATE_REF_AUTHOR="FK_TEMPLATE_REF_AUTHOR";

    String FK_TEMPLATE_DOCUMENTS="FK_TEMPLATE_DOCUMENTS";
    String FK_DOCUMENTS_SHAREDWITH="FK_DOCUMENTS_SharedWith";
    String FK_DOCUMENTS_COMMENTS="FK_DOCUMENTS_comments";
    String FK_TAGS_REF_TEMPLATE_FILE="FK_TAGS_REF_template_FILE";
    String FK_DOCCOMMENT_DOCUMENT="FK_DOCCOMMENT_DOCUMENT";
String FK_ADDITIONAL_FILE_REF_TEMPLATE="FK_ADDITIONAL_FILE_REF_TEMPLATE";
}
