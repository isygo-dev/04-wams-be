package eu.isygoit.enums;

public interface IEnumCategoryStatus {
    int STR_ENUM_SIZE = 10;

    enum Status implements IEnum {
        ENABLED("Enabled"),
        DISABLED("Disabled");
        private final String meaning;

        Status(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
