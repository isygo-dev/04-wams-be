package eu.isygoit.exception;


import eu.isygoit.annotation.MsgLocale;


/**
 * The type Resume not found exception.
 */
@MsgLocale("resume.not.found.exception")
public class ResumeNotFoundException extends ManagedException {

    /**
     * Instantiates a new Resume not found exception.
     *
     * @param s the s
     */
    public ResumeNotFoundException(String s) {
    }
}
