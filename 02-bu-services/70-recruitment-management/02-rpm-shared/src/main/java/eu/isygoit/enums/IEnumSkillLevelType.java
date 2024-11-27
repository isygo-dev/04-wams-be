package eu.isygoit.enums;

/**
 * The interface Enum skill level type.
 */
public interface IEnumSkillLevelType {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Expert types.
         */
        EXPERT("Expert"),
        /**
         * Confirmed types.
         */
        CONFIRMED("Confirmed"),
        /**
         * Intermediate types.
         */
        INTERMEDIATE("Intermediate"),
        /**
         * Beginner types.
         */
        BEGINNER("Beginner");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
