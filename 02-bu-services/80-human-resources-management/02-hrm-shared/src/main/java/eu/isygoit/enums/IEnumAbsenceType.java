package eu.isygoit.enums;

/**
 * The interface Enum absence type.
 */
public interface IEnumAbsenceType {
    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 35;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {

        /**
         * Annual leave types.
         */
        ANNUAL_LEAVE("Annual_Leave"),

        /**
         * Autorisation types.
         */
        AUTORISATION("Autorisation"),

        /**
         * Sick leave types.
         */
        SICK_LEAVE("Sick_Leave"),

        /**
         * Maternity leave types.
         */
        MATERNITY_LEAVE("Maternity_Leave"),

        /**
         * Paternity leave types.
         */
        PATERNITY_LEAVE("Paternity_Leave"),

        /**
         * Unpaid leave types.
         */
        UNPAID_LEAVE("Unpaid_Leave"),

        /**
         * Half day leave types.
         */
        HALF_DAY_LEAVE("Half_Day_Leave"),

        /**
         * Special leave types.
         */
        SPECIAL_LEAVE("Special_Leave"),

        /**
         * Training leave types.
         */
        TRAINING_LEAVE("Training_Leave"),

        /**
         * Public holiday types.
         */
        PUBLIC_HOLIDAY("Public_Holiday"),

        /**
         * Recovery holiday types.
         */
        RECOVERY_HOLIDAY("Recovery_Holiday"),

        /**
         * Compensatory leave types.
         */
        COMPENSATORY_LEAVE("Compensatory_Leave"),


        /**
         * Other types.
         */
        OTHER("Other"),


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
