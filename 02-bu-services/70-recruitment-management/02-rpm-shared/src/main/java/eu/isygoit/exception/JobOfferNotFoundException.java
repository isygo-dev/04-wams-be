package eu.isygoit.exception;


import eu.isygoit.annotation.MsgLocale;


/**
 * The type Resume not found exception.
 */
@MsgLocale("job.offer.not.found.exception")
public class JobOfferNotFoundException extends ManagedException {

    /**
     * Instantiates a new Resume not found exception.
     *
     * @param s the s
     */
    public JobOfferNotFoundException(String s) {
    }
}
