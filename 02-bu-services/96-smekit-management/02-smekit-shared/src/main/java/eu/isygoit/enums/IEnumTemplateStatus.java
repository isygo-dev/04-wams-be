package eu.isygoit.enums;

public interface IEnumTemplateStatus {
    int STR_ENUM_SIZE = 15;

    enum Types implements IEnum {
        EDITING("Editing"),
        VALIDATING("Validated"),
        REJECTED("Rejected");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
