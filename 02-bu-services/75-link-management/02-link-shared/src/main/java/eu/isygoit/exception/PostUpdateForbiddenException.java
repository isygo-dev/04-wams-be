package eu.isygoit.exception;


import eu.isygoit.annotation.MsgLocale;


/**
 * The type Post update forbidden exception.
 */
@MsgLocale("post.update.forbidden.exception")
public class PostUpdateForbiddenException extends ManagedException {

    /**
     * Instantiates a new Post update forbidden exception.
     *
     * @param s the s
     */
    public PostUpdateForbiddenException(String s) {
    }
}
