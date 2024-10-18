package eu.isygoit.enums;

/**
 * The interface Enum contract type.
 */
public interface IEnumContractType {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Cdi types.
         */
        CDI("cdi"),
        /**
         * Cdd types.
         */
        CDD("cdd"),
        /**
         * Internship types.
         */
        INTERNSHIP("internship"),
        /**
         * Interim types.
         */
        INTERIM("interim");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
