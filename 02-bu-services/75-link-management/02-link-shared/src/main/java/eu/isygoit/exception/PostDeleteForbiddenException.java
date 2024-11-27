package eu.isygoit.exception;


import eu.isygoit.annotation.MsgLocale;


/**
 * The type Post delete forbidden exception.
 */
@MsgLocale("post.delete.forbidden.exception")
public class PostDeleteForbiddenException extends ManagedException {

    /**
     * Instantiates a new Post delete forbidden exception.
     *
     * @param s the s
     */
    public PostDeleteForbiddenException(String s) {
    }
}
