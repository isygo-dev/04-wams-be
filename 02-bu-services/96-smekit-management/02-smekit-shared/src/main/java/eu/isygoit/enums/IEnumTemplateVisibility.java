package eu.isygoit.enums;

public interface IEnumTemplateVisibility {
    int STR_ENUM_SIZE = 5;

    enum Types implements IEnum {
        PB("Public"),
        PRV("Private");

        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
