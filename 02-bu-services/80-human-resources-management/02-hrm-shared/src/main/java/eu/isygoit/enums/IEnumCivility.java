package eu.isygoit.enums;


/**
 * The interface Enum civilities.
 */
public interface IEnumCivility {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 10;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Single types.
         */
        SINGLE("Single"),
        /**
         * Married types.
         */
        MARRIED("Married"),
        /**
         * Divorced types.
         */
        DIVORCED("Divorced");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return this.meaning;
        }
    }
}
