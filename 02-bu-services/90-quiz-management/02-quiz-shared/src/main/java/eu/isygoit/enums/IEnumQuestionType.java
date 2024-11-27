package eu.isygoit.enums;


/**
 * The interface Enum question type.
 */
public interface IEnumQuestionType {

    /**
     * The constant STR_ENUM_SIZE.
     */
    int STR_ENUM_SIZE = 10;

    /**
     * The enum Types.
     */
    enum Types implements IEnum {
        /**
         * The Mcq.
         */
        MCQ("One choice question"),
        /**
         * The Taq.
         */
        TAQ("Text answer question"),
        /**
         * The Mctaq.
         */
        MCTAQ("Multiple choice with text answer question"),

        /**
         * The Mcqm.
         */
        MCQM("Multiple choice "),


        /**
         * The Mcqa.
         */
        MCQA("One choice with text answer question"),

        /**
         * The CODE.
         */
        CODE("CODING");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
