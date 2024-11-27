package eu.isygoit.enums;


/**
 * The interface Enum resume stat type.
 */
public interface IEnumResumeStatType {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 30;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Total count types.
         */
        TOTAL_COUNT("TOTAL_COUNT"),
        /**
         * Uploaded by me count types.
         */
        UPLOADED_BY_ME_COUNT("UPLOADED_BYME_COUNT"),
        /**
         * Confirmed count types.
         */
        CONFIRMED_COUNT("CONFIRMED_COUNT"),
        /**
         * Completed count types.
         */
        COMPLETED_COUNT("COMPLETED_COUNT"),
        /**
         * Interviewed count types.
         */
        INTERVIEWED_COUNT("INTERVIEWED_COUNT");
        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return this.meaning;
        }
    }
}
