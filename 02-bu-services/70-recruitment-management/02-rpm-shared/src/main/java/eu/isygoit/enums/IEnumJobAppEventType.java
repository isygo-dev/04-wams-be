package eu.isygoit.enums;

/**
 * The interface Enum job app event type.
 */
public interface IEnumJobAppEventType {
    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Meeting types.
         */
        MEETING("Meeting"),
        /**
         * Interview types.
         */
        INTERVIEW("Interview"),
        /**
         * Contract types.
         */
        CONTRACT("Contract");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
