package eu.isygoit.enums;

/**
 * The interface Enum time type.
 */
public interface IEnumTimeType {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Fulltime types.
         */
        FULLTIME("Full-Time"),
        /**
         * Parttime types.
         */
        PARTTIME("Part-Time"),
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
