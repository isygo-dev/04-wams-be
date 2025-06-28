package eu.isygoit.enums;

public interface IEnumPermissionLevel {

    int STR_ENUM_SIZE = 10;

    enum PermissionLevel implements IEnum {
        READ("READ"),
        EDIT("EDIT");

        private final String meaning;

        PermissionLevel(String meaning) {
            this.meaning = meaning;
        }

        public String meaning() {
            return meaning;
        }
    }
}
