package eu.isygoit.enums;

/**
 * The interface Enum insurance type.
 */
public interface IEnumInsuranceType {
    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 40;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * The Health insurance.
         */
        HEALTH_INSURANCE("Health Insurance"),

        /**
         * The Social security.
         */
        SOCIAL_SECURITY("Social Security");
        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }

}
