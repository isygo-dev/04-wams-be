package eu.isygoit.exception;


import eu.isygoit.annotation.MsgLocale;

/**
 * The type Post comment not found exception.
 */
@MsgLocale("post.comment.not.found.exception")
public class PostCommentNotFoundException extends ManagedException {


    /**
     * Instantiates a new Post comment not found exception.
     *
     * @param s the s
     */
    public PostCommentNotFoundException(String s) {
    }
}
