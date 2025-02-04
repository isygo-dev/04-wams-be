package eu.isygoit.exception;


import eu.isygoit.annotation.MsgLocale;


/**
 * The type Resume not found exception.
 */
@MsgLocale("job.offer.app.not.found.exception")
public class JobOfferAppNotFoundException extends ManagedException {

    /**
     * Instantiates a new Resume not found exception.
     *
     * @param s the s
     */
    public JobOfferAppNotFoundException(String s) {
    }
}
