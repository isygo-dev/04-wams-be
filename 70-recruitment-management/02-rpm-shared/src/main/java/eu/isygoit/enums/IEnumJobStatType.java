package eu.isygoit.enums;


/**
 * The interface Enum job stat type.
 */
public interface IEnumJobStatType {

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
         * Active count types.
         */
        ACTIVE_COUNT("ACTIVE_COUNT"),
        /**
         * Confirmed count types.
         */
        CONFIRMED_COUNT("CONFIRMED_COUNT");
        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return this.meaning;
        }
    }
}
