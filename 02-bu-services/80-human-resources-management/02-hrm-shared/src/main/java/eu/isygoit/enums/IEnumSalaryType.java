package eu.isygoit.enums;

/**
 * The interface Enum salary type.
 */
public interface IEnumSalaryType {
    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 40;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {

        /**
         * Fixed types.
         */
        FIXED("Fixed"),
        /**
         * Variable types.
         */
        VARIABLE("Variable"),
        ;
        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }

}
