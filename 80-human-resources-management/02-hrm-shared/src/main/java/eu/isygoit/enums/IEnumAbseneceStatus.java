package eu.isygoit.enums;

/**
 * The interface Enum absenece status.
 */
public interface IEnumAbseneceStatus {


    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 16;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {

        /**
         * Created types.
         */
        CREATED("CREATED"),
        /**
         * Pending types.
         */
        PENDING("PENDING"),
        /**
         * Rejected types.
         */
        REJECTED("REJECTED"),
        /**
         * Accepted types.
         */
        ACCEPTED("ACCEPTED");


        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}