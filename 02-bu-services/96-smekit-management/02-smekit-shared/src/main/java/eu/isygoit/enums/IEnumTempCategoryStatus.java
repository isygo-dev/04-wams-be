package eu.isygoit.enums;

public interface IEnumTempCategoryStatus {
    int STR_ENUM_SIZE = 10;
    enum Types implements IEnum {
        ENABLED("Enabled"),
        DISABLED("Disabled");
        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
    }
