package eu.isygoit.enums;

/**
 * The interface Enum job app status type.
 */
public interface IEnumJobAppStatusType {
    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Planned types.
         */
        PLANNED("Planned"),
        /**
         * Done types.
         */
        DONE("Done"),
        /**
         * Postpoint types.
         */
        POSTPOINT("PostPoint"),
        /**
         * Cancelled types.
         */
        CANCELLED("Cancelled");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
