package eu.isygoit.enums;

public interface IEnumDocCommentsStaus {
    int STR_ENUM_SIZE = 10;

    enum Types implements IEnum {
        OPEN("open"),
        CLOSED("Closed"),
        VALIDATED("Validation");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
