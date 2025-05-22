package eu.isygoit.enums;

public interface IEnumTemplateLanguage {
    int STR_ENUM_SIZE = 5;
    enum Types implements IEnum {
        EN("English"),
        FR("French"),
        AR("Arabic"),
        DE("German"),
        SPA("Spanish"),
        ITA("Italian");
        private final String meaning;

        Types(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
