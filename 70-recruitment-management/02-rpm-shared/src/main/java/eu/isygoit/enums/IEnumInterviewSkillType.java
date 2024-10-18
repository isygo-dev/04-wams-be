package eu.isygoit.enums;

/**
 * The interface Enum interview skill type.
 */
public interface IEnumInterviewSkillType {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 12;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * Resume types.
         */
        RESUME("resume"),
        /**
         * Job types.
         */
        JOB("Job"),
        /**
         * New types.
         */
        NEW("new");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
