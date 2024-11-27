package eu.isygoit.enums;

/**
 * The interface Enum skill type.
 */
public interface IEnumSkillType {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum { // Make 'Types' enum public
        /**
         * Soft types.
         */
        SOFT("SOFT"),
        /**
         * Hard types.
         */
        HARD("HARD");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
